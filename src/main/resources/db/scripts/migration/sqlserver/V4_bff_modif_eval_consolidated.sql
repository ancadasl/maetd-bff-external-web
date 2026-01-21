CREATE OR ALTER TRIGGER [marketcreators_evaluation].[eval_consolidate]
ON [marketcreators_evaluation].[evaluation]
AFTER UPDATE
AS

DECLARE @v_id AS INT;
DECLARE @new_id AS INT;
DECLARE @new_status AS VARCHAR(255);
DECLARE @new_date AS DATE;
DECLARE @v_fecha AS DATE;
DECLARE @v_date AS INT;

BEGIN

IF UPDATE(status)
--IF '1' = '2'
BEGIN
  SELECT @new_id = id
    FROM inserted;

  SELECT @new_status=status
    FROM inserted;
	
  SELECT @new_date=evaluation_year_month
    FROM inserted;
  
  TRUNCATE TABLE marketcreators_datahw.cotizaciones;
  
  INSERT INTO marketcreators_datahw.cotizaciones
  SELECT * 
  FROM (SELECT bonos.entidad, 
       bonos.fecha, 
	   bonos.cumplimientoBonos,
	   letras.cumplimientoLetras,
       sobrecumpBonos,
	   sobrecumpStrips,
	   sobrecumpRefBonos,
	   sobrecumpRefLetras,
	   sobrecumpRefStrips,
	   valorNumCotizacionBonos,
	   valorNumCotizacionLetras,
	   valorNumCotizacionStrips
  FROM (SELECT cb.entidad, 
		       cb.fecha,
        	   cb.promedio as cumplimientoBonos,
        	   sib.promedio as sobrecumpBonos,
        	   srb.promedio as sobrecumpRefBonos,
        	   vncb.promedio as valorNumCotizacionBonos,
        	   'BOND' as tipoProducto
          FROM (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_cumplimientos_bonos GROUP BY entidad,fecha,promedio) as cb,
        	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_sobrecump_indices_byo GROUP BY entidad,fecha,promedio) as sib,
        	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_sobrecump_ref_byo GROUP BY entidad,fecha,promedio) as srb,
        	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_vnc_byo GROUP BY entidad,fecha,promedio) as vncb
         WHERE cb.entidad=sib.entidad
        	   AND vncb.entidad=cb.entidad
        	   AND srb.entidad=cb.entidad
        	   AND cb.fecha=sib.fecha
        	   AND vncb.fecha=cb.fecha
        	   AND srb.fecha=cb.fecha) as bonos,
        (SELECT st.entidad, 
		       st.fecha,
        	   st.promedio as sobrecumpStrips,
        	   srt.promedio as sobrecumpRefStrips,
        	   vncs.promedio as valorNumCotizacionStrips,
        	   'BOND' as tipoProducto
          FROM (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_sobrecump_indices_strips GROUP BY entidad,fecha,promedio) as st,
               (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_sobrecump_ref_strips GROUP BY entidad,fecha,promedio) as srt,
        	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_vnc_strips GROUP BY entidad,fecha,promedio) as vncs
         WHERE vncs.entidad=st.entidad
        	   AND srt.entidad=st.entidad
        	   AND st.fecha=vncs.fecha
        	   AND srt.fecha=st.fecha) as strips,
       (SELECT cl.entidad, 
	           cl.fecha,
	           cl.promedio as cumplimientoLetras,
	           sl.promedio as sobrecumpRefLetras,
	           vnc.promedio as valorNumCotizacionLetras,
	           'LETTER' as tipoProducto
          FROM (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_cumplimientos_letras GROUP BY entidad,fecha,promedio) as cl,
         	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_sobrecump_ref GROUP BY entidad,fecha,promedio) as sl,
         	   (SELECT entidad, fecha, promedio, MAX(version) as version FROM marketcreators_ext.promedio_mensual_vnc GROUP BY entidad,fecha,promedio) as vnc
         WHERE cl.entidad=sl.entidad
         	   AND vnc.entidad=cl.entidad
         	   AND cl.fecha=sl.fecha
			   AND vnc.fecha=sl.Fecha) as letras
WHERE bonos.entidad=strips.entidad 
  AND bonos.entidad=letras.entidad
  AND bonos.fecha=strips.fecha
  AND bonos.fecha=letras.fecha) as T;
	
  IF @new_status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED')
  BEGIN
    SET @v_id=(SELECT CASE 
	                  WHEN EXISTS (SELECT *
                                    FROM marketcreators_datahw.primario_general
                                  WHERE evaluation_id=@new_id)
                      THEN CAST(1 AS BIT)
                      ELSE CAST(0 AS BIT) END);
	  
	 IF @v_id=1 
	 BEGIN
     -- Actualizar datos de evaluación existente	 
	    -- Datos primario
        UPDATE marketcreators_datahw.primario_general
           SET puntosPorMillon=t2.puntosPorMillon,
               subastasComputables=t2.subastasComputables,
               VolumenSuscPond=t2.VolumenSuscPond,
               participacion=t2.participacion,
               prima=t2.prima,
			   total=t2.total,
			   otros=t2.otros,
	           recompras=t2.recompras,
               puntosPorMillonconPenaliz=t2.puntosPorMillonconPenaliz
          FROM marketcreators_datahw.primario_general AS pg,
               (SELECT evaluation_id,
			           market_creator_id,
                       entidad,
                       fecha,
                       CASE 
        		        WHEN product_type='LETTER' THEN 0.03*VolumenSuscPond
        		        ELSE 0.0051*VolumenSuscPond
        	           END AS puntosPorMillon,
                       subastasComputables,
        	           VolumenSuscPond,
        	           participacion,
        	           prima,
					   CASE 
		                WHEN product_type='LETTER' THEN (0.03*VolumenSuscPond)+participacion
		                ELSE (0.0051*VolumenSuscPond)+participacion
	                   END AS total,
        	           totalPrimario,
        	           puntosPorMillonconPenaliz,
					   otros,
	                   recompras,
        	           product_type
                  FROM (SELECT pb.market_creator_id,
            		           pb.evaluation_id,
        			           e.fecha                                                                 AS fecha,
            		           name                                                                    AS entidad, 
            		           pe.computable_sale                                                      AS subastasComputables,
                 	           SUM(subscribed_penalizated_weighted+second_round_awarded_weighted)/1000 AS VolumenSuscPond,
            		           SUM(participation_penalized)                                            AS participacion, 
            		           SUM(prima)                                                              AS prima,
               		           total_points                                                            AS totalPrimario,
            		           total_points-penalty                                                    AS puntosPorMillonconPenaliz,
							   value_others                                                            AS otros,
			                   exchange                                                                AS recompras,
            		           product_type
            	          FROM marketcreators_evaluation.primary_bidding_values as pb, 
            		           marketcreators_creatordata.market_creator as mc,
            		           marketcreators_evaluation.primary_evaluations_values as pe,
            		           marketcreators_evaluation.bidding as b,
        			           marketcreators_datahw.evaluation_aux as e
            	         WHERE pb.market_creator_id=mc.id
            	           AND pe.evaluation_id=pb.evaluation_id
        		           AND e.id=pe.evaluation_id
            	           AND pb.market_creator_id=pe.market_creator_id
            	           AND pb.bidding_id=b.id
            	           AND product_type=type
				           AND e.id=@new_id
            	        GROUP BY pb.market_creator_id,
            		             pb.evaluation_id,
            		             name,
        			             e.fecha,
            		             pe.computable_sale,
            		             total_points,
            		             total_points-penalty,
								 value_others,
			                     exchange,
            		             product_type) AS t
			   ) as t2
	     WHERE pg.entidad=t2.entidad
           AND pg.fecha=t2.fecha
           AND pg.evaluation_id=t2.evaluation_id
           AND pg.product_type=t2.product_type
		   AND pg.evaluation_id=@new_id;

	    -- Datos secundario
        UPDATE marketcreators_datahw.secundario_general
           SET MAX27Cotizacion            =t.MAX27Cotizacion,
		       valoracion8senaf           =t.valoracion8senaf,
		       negSenafMts                =t.negSenafMts,
		       valoracion7agred           =t.valoracion7agred,
		       negAgredAgres              =t.negAgredAgres,
			   valoracionEmts             =t.valoracionEmts,
			   negEuroMts                 =t.negEuroMts,
		       valoracion4other           =t.valoracion4other,
		       negOtherElec               =t.negOtherElec,
		       valoracion8elec            =t.valoracion8elec,
		       negElec                    =t.negElec, 
		       valoracion15third          =t.valoracion15third,
		       negThird                   =t.negThird,
		       valorNumericoCotiz         =t.valorNumericoCotiz,
		       valoracion12cotiz          =t.valoracion12cotiz,
		       totalSobrecumplimiento     =t.totalSobrecumplimiento,
		       valoracion15sobreCumpl     =t.valoracion15sobreCumpl,
		       vncPromedioStrip           =t.vncPromedioStrip,
		       valoracionStripPromedio    =t.valoracionStripPromedio,
		       totalSobrecumplimientoStrip=t.totalSobrecumplimientoStrip,
		       valoracionStrip            =t.valoracionStrip,	
               strips                     =t.strips,			   
		       cumplimientoObl            =t.cumplimientoObl,
			   valoracionManualCump       =t.valoracionManualCump,
		       sumaTotal                  =t.sumaTotal,
		       web                        =t.web,
			   valoracionSpreadHorasByO   =t.valoracionSpreadHorasByO,
               sobrecSpreadHorasByO       =t.sobrecSpreadHorasByO,
			   valoracionTotalCotizByO    =t.valoracionTotalCotizByO,
			   valoracionSpreadHorasStrips=t.valoracionSpreadHorasStrips,
               sobrecSpreadHorasStrips    =t.sobrecSpreadHorasStrips,
			   primeroCotizar             =t.primeroCotizar
		  FROM marketcreators_datahw.secundario_general as sg,
             (SELECT se.market_creator_id,
			         se.evaluation_id,
			         e.fecha                                                                          AS fecha,
			         name                                                                             AS entidad,
       	             total_amount_quotation                                                           AS MAX27Cotizacion,
       	             senaf_mts_point                                                                  AS valoracion8senaf,
       	             senaf_mts                                                                        AS negSenafMts,
       	             agres_senaf_mts_point                                                            AS valoracion7agred,
       	             agres_agred_new                                                                  AS negAgredAgres,
			         emts_point                                                                       AS valoracionEmts,
			         emts                                                                             AS negEuroMts,
       	             other_electronic_point                                                           AS valoracion4other,
       	             other_electronic                                                                 AS negOtherElec,
       	             non_electronic_point                                                             AS valoracion8elec,
       	             non_electronic                                                                   AS negElec, 
       	             third_point                                                                      AS valoracion15third,
       	             third                                                                            AS negThird,
       	             vnc                                                                              AS valorNumericoCotiz,
       	             vnc_point                                                                        AS valoracion12cotiz,
       	             benchmark_overachievement                                                        AS totalSobrecumplimiento,
       	             benchmark_overachievement_point                                                  AS valoracion15sobreCumpl,
			         vnc_strip                                                                        AS vncPromedioStrip,
			         vnc_strip_point                                                                  AS valoracionStripPromedio,
			         benchmark_overachievement_strip                                                  AS totalSobrecumplimientoStrip,
			         benchmark_overachievement_strip_point                                            AS valoracionStrip,	
                     strips_pond+strips_other_electr+strips_non_electr+strips_thirds                  AS strips,			   
       	             basket                                                                           AS cumplimientoObl,
					 penalty_point_basket                                                             AS valoracionManualCump,
			         total_amount_quotation                                                           AS sumaTotal,
                     benchmark_overachievement_point+vnc_point                                        AS web,			   
					 benchmark_overachievement_spread_point                                           AS valoracionSpreadHorasByO,
			         sobrecumpBonos                                                                   AS sobrecSpreadHorasByO,
			         vnc_point+benchmark_overachievement_point+benchmark_overachievement_spread_point AS valoracionTotalCotizByO,			   
                     benchmark_overachievement_spread_strips_point                                    AS valoracionSpreadHorasStrips,
			         sobrecumpStrips                                                                  AS sobrecSpreadHorasStrips,
			         premium_point                                                                    AS primeroCotizar,
			         se.product_type,
					 CASE WHEN se.product_type='LETTER' THEN 'LETRAS'
        		       ELSE 'BONOS'
        	          END AS tipo
                FROM marketcreators_evaluation.secundary_evaluations_neg_values as se,
           	         marketcreators_evaluation.secundary_evaluations_quote_values as sq,
           	         marketcreators_creatordata.market_creator as mc,
    			     marketcreators_datahw.cotizaciones as c,
    			     marketcreators_datahw.evaluation_aux as e
               WHERE se.market_creator_id=mc.id
                 AND sq.evaluation_id=se.evaluation_id
                 AND sq.market_creator_Id=se.market_creator_id
                 AND se.product_type=sq.product_type
    		     AND c.entidad=mc.csb
    		     AND c.fecha=e.fecha
    		     AND e.id=sq.evaluation_id
    		     AND e.id=@new_id) AS t
       WHERE sg.entidad=t.entidad
         AND sg.fecha=t.fecha
         AND sg.evaluation_id=t.evaluation_id
         AND sg.product_type=t.product_type
		 AND sg.evaluation_id=@new_id;
	 
	
	 -- Datos primario original y ponderado
	    UPDATE marketcreators_datahw.originalYponderado
		   SET primVuelta=t.primVuelta,
               primVueltaPonderado=t.primVueltaPonderado,
               primVueltaPenalizado=t.primVueltaPenalizado,
               primVueltaPenalPond=t.primVueltaPenalPond,
               noCompetitivo=t.noCompetitivo,
               noCompPonderado=t.noCompPonderado,
               segVueltaPond=t.segVueltaPond,
               segVuelta=t.segVuelta,
			   TotalComp=t.TotalComp,
			   percent_resolution=t.percent_resolution,
			   noAdjComp=t.noAdjComp,
			   awarded_competitive=t.awarded_competitive,
			   participation_penalized=t.participation_penalized,
			   prima=t.prima,
			   nomSolicitado=t.nomSolicitado,
			   nomAdjudicado=t.nomAdjudicado,
			   Onc=t.Onc,
			   noCompAdjudicada=t.noCompAdjudicada,
			   noCompSolicitada=t.noCompSolicitada
		  FROM marketcreators_datahw.originalYponderado AS op,
		       (SELECT title
                      ,pb.bidding_id
					  ,bid_date
                	  ,pb.market_creator_id
                      ,name            
                      ,subscribed                                            AS 'primVuelta'
                      ,subscribed_weighted                                   AS 'primVueltaPonderado'
                      ,subscribed_penalized                                  AS 'primVueltaPenalizado'
                      ,subscribed_penalizated_weighted                       AS 'primVueltaPenalPond'
                      ,subscribed_non_comput                                 AS 'noCompetitivo'
                      ,subscribed_weighted_non_comput                        AS 'noCompPonderado'
                      ,second_round_awarded_weighted                         AS 'segVueltaPond'
                	  ,second_round_awarded                                  AS 'segVuelta'
                	  ,nominal_computable/1000                               AS 'TotalComp'
                      ,percent_resolution                                    
			          ,awarded_non_computable/1000                           AS 'noAdjComp'
			          ,awarded_competitive
					  ,participation_penalized
			          ,prima
			          ,requested/1000000                                     AS 'nomSolicitado'
			          ,(awarded_competitive+awarded_non_competitive)/1000000 AS 'nomAdjudicado'
			          ,subscribed_non_comput/1000                            AS 'Onc'
					  ,nocompetitive_offer_awarded                           AS 'noCompAdjudicada'
			          ,nocompetitive_offer_requested                         AS 'noCompSolicitada'
					  ,CASE WHEN b.type='LETTER' THEN 'LETRAS'
        		        ELSE 'BONOS'
        	           END AS tipo
					  ,b.type
                	  ,e.id
					  ,e.fecha
                  FROM marketcreators_evaluation.primary_bidding_values as pb,
                       marketcreators_evaluation.bidding as b,
                	   marketcreators_datahw.evaluation_aux AS e,
                	   marketcreators_creatordata.market_creator as m,
                	   marketcreators_evaluation.second_round as sr,
                	   marketcreators_evaluation.second_round_entry as sre,
			           marketcreators_evaluation.participation_detail as p,
		               marketcreators_evaluation.participation_detail_entry as pe
                 WHERE pb.bidding_id=b.id
                   AND pb.evaluation_id=e.id
                   AND sr.isin=b.isin
		           AND sr.tramo=b.tramo
                   AND sr.id=sre.second_round_id
                   AND m.id=pb.market_creator_id
                   AND pb.market_creator_id=sre.market_creator_id
		           AND b.isin=p.isin
                   AND b.bid_date=p.date
                   AND b.tramo=p.tramo
                   AND p.id=pe.participation_detail_id   
                   AND pb.market_creator_id=pe.market_creator_id
                   AND e.fecha=DATEADD(DAY, 1, EOMONTH(b.bid_date, -1))
				   AND e.id=@new_id) as T
		WHERE op.id=t.id
		  AND op.id=@new_id
		  AND op.title=t.title
		  AND op.name=t.name
		  AND op.type=t.type
		  AND op.bid_date=t.bid_date
		  AND op.fecha=t.fecha
		  AND op.bidding_id=t.bidding_id;
		  
	 END;	  
	ELSE	  
	BEGIN
	    SET @v_date=(SELECT CASE 
	                  WHEN EXISTS (SELECT *
                                    FROM marketcreators_datahw.primario_general
                                  WHERE fecha=@new_date)
                      THEN CAST(1 AS BIT)
                      ELSE CAST(0 AS BIT) END);
		
		IF @v_date=1 
		BEGIN
		   DELETE FROM marketcreators_datahw.primario_general
		   WHERE fecha=@new_date;
		   
		   DELETE FROM marketcreators_datahw.secundario_general
		   WHERE fecha=@new_date;
		   
		   DELETE FROM marketcreators_datahw.originalYponderado
		   WHERE fecha=@new_date;
		END;
		
	-- Nueva evaluación
	  	-- Datos primario
		INSERT INTO marketcreators_datahw.primario_general
        SELECT evaluation_id,
               market_creator_id,
               entidad,
               fecha,
               CASE 
        		WHEN product_type='LETTER' THEN 0.03*VolumenSuscPond
        		ELSE 0.0051*VolumenSuscPond
        	   END AS puntosPorMillon,
               subastasComputables,
        	   VolumenSuscPond,
        	   participacion,
        	   prima,
        	    CASE 
        		WHEN product_type='LETTER' THEN (0.03*VolumenSuscPond)+participacion
        		ELSE (0.0051*VolumenSuscPond)+participacion
        	   END AS total,
        	   puntosPorMillonconPenaliz,
        	   otros,
        	   recompras,
        	   product_type
          FROM (SELECT pb.market_creator_id,
            		   pb.evaluation_id,
        			   e.evaluation_year_month                                                 AS fecha,
            		   name                                                                    AS entidad, 
            		   pe.computable_sale                                                      AS subastasComputables,
                 	   SUM(subscribed_penalizated_weighted+second_round_awarded_weighted)/1000 AS VolumenSuscPond,
            		   SUM(participation_penalized)                                            AS participacion, 
            		   SUM(prima)                                                              AS prima,
               		   total_points                                                            AS totalPrimario,
            		   total_points-penalty                                                    AS puntosPorMillonconPenaliz,
        			   value_others                                                            AS otros,
        			   exchange                                                                AS recompras,
            		   product_type
            	  FROM marketcreators_evaluation.primary_bidding_values as pb, 
            		   marketcreators_creatordata.market_creator as mc,
            		   marketcreators_evaluation.primary_evaluations_values as pe,
            		   marketcreators_evaluation.bidding as b,
        			   marketcreators_evaluation.evaluation as e
            	 WHERE pb.market_creator_id=mc.id
            	   AND pe.evaluation_id=pb.evaluation_id
        		   AND e.id=pe.evaluation_id
            	   AND pb.market_creator_id=pe.market_creator_id
            	   AND pb.bidding_id=b.id
            	   AND product_type=type
				   AND e.id=@new_id
            	GROUP BY pb.market_creator_id,
            		   pb.evaluation_id,
            		   name,
        			   e.evaluation_year_month,
            		   pe.computable_sale,
            		   total_points,
            		   total_points-penalty,
        			   value_others,
        			   exchange,
            		   product_type) as T;  
    	    
    	-- Datos secundario
        INSERT INTO marketcreators_datahw.secundario_general
        SELECT se.market_creator_id,
			   se.evaluation_id,
			   e.evaluation_year_month                                                          AS fecha,
			   name                                                                             AS entidad,
       	       total_amount_quotation                                                           AS MAX27Cotizacion,
       	       senaf_mts_point                                                                  AS valoracion8senaf,
       	       senaf_mts                                                                        AS negSenafMts,
       	       agres_senaf_mts_point                                                            AS valoracion7agred,
       	       agres_agred_new                                                                  AS negAgredAgres,
			   emts_point                                                                       AS valoracionEmts,
			   emts                                                                             AS negEuroMts,
       	       other_electronic_point                                                           AS valoracion4other,
       	       other_electronic                                                                 AS negOtherElec,
       	       non_electronic_point                                                             AS valoracion8elec,
       	       non_electronic                                                                   AS negElec, 
       	       third_point                                                                      AS valoracion15third,
       	       third                                                                            AS negThird,
       	       vnc                                                                              AS valorNumericoCotiz,
       	       vnc_point                                                                        AS valoracion12cotiz,
       	       benchmark_overachievement                                                        AS totalSobrecumplimiento,
       	       benchmark_overachievement_point                                                  AS valoracion15sobreCumpl,
			   vnc_strip                                                                        AS vncPromedioStrip,
			   vnc_strip_point                                                                  AS valoracionStripPromedio,
			   benchmark_overachievement_strip                                                  AS totalSobrecumplimientoStrip,
			   benchmark_overachievement_strip_point                                            AS valoracionStrip,	
               strips_pond+strips_other_electr+strips_non_electr+strips_thirds                  AS strips,			   
       	       basket                                                                           AS cumplimientoObl,
			   penalty_point_basket                                                             AS valoracionManualCump,
			   total_amount_quotation                                                           AS sumaTotal,
               benchmark_overachievement_point+vnc_point                                        AS web,
			   benchmark_overachievement_spread_point                                           AS valoracionSpreadHorasByO,
			   sobrecumpBonos                                                                   AS sobrecSpreadHorasByO,
			   vnc_point+benchmark_overachievement_point+benchmark_overachievement_spread_point AS valoracionTotalCotizByO,			   
               benchmark_overachievement_spread_strips_point                                    AS valoracionSpreadHorasStrips,
			   sobrecumpStrips                                                                  AS sobrecSpreadHorasStrips,
			   premium_point                                                                    AS primeroCotizar,
			   se.product_type,
			   CASE WHEN se.product_type='LETTER' THEN 'LETRAS'
        		ELSE 'BONOS'
               END AS tipo
          FROM marketcreators_evaluation.secundary_evaluations_neg_values as se,
       	       marketcreators_evaluation.secundary_evaluations_quote_values as sq,
       	       marketcreators_creatordata.market_creator as mc,
			   marketcreators_datahw.cotizaciones as c,
			   marketcreators_evaluation.evaluation as e
         WHERE se.market_creator_id=mc.id
           AND sq.evaluation_id=se.evaluation_id
           AND sq.market_creator_Id=se.market_creator_id
           AND se.product_type=sq.product_type
		   AND c.entidad=mc.csb
		   AND c.fecha=e.evaluation_year_month
		   AND e.id=sq.evaluation_id
		   AND e.id=@new_id;
    	
        -- Datos primario Original y ponderado
        INSERT INTO marketcreators_datahw.originalYponderado
            SELECT title
                  ,pb.bidding_id
				  ,b.isin
			      ,b.tramo
                  ,bid_date
        	      ,pb.market_creator_id
                  ,name            
                  ,subscribed                                            AS 'primVuelta'
                  ,subscribed_weighted                                   AS 'primVueltaPonderado'
                  ,subscribed_penalized                                  AS 'primVueltaPenalizado'
                  ,subscribed_penalizated_weighted                       AS 'primVueltaPenalPond'
                  ,subscribed_non_comput                                 AS 'noCompetitivo'
                  ,subscribed_weighted_non_comput                        AS 'noCompPonderado'
                  ,second_round_awarded_weighted                         AS 'segVueltaPond'
        	      ,second_round_awarded                                  AS 'segVuelta'
			      ,nominal_computable/1000                               AS 'TotalComp'
                  ,percent_resolution                                    
			      ,awarded_non_computable/1000                           AS 'noAdjComp'
			      ,awarded_competitive                                   
				  ,participation_penalized                               
			      ,prima                                                 
        	      ,requested/1000000                                     AS 'nomSolicitado'
			      ,(awarded_competitive+awarded_non_competitive)/1000000 AS 'nomAdjudicado'
			      ,subscribed_non_comput/1000                            AS 'Onc'
				  ,nocompetitive_offer_awarded                           AS 'noCompAdjudicada'
			      ,nocompetitive_offer_requested                         AS 'noCompSolicitada'
				  ,b.type
				  ,CASE WHEN b.type='LETTER' THEN 'LETRAS'
        		    ELSE 'BONOS'
        	       END AS tipo
        	      ,e.id
        	      ,e.fecha
             FROM marketcreators_evaluation.primary_bidding_values as pb,
                  marketcreators_evaluation.bidding as b,
        	      marketcreators_datahw.evaluation_aux AS e,
        	      marketcreators_creatordata.market_creator as m,
        	      marketcreators_evaluation.second_round as sr,
        	      marketcreators_evaluation.second_round_entry as sre,
			      marketcreators_evaluation.participation_detail as p,
		          marketcreators_evaluation.participation_detail_entry as pe
            WHERE pb.bidding_id=b.id
              AND pb.evaluation_id=e.id
              AND sr.isin=b.isin
		      AND sr.tramo=b.tramo
              AND sr.id=sre.second_round_id
              AND m.id=pb.market_creator_id
              AND pb.market_creator_id=sre.market_creator_id
		      AND b.isin=p.isin
              AND b.bid_date=p.date
              AND b.tramo=p.tramo
              AND p.id=pe.participation_detail_id   
              AND pb.market_creator_id=pe.market_creator_id
              AND e.fecha=DATEADD(DAY, 1, EOMONTH(b.bid_date, -1))
              AND e.id=@new_id;				
	   END;

       -- Carga de tablas de historico
	   SELECT @v_fecha=CAST(evaluation_year_month AS DATE)	     
		 FROM marketcreators_evaluation.evaluation
		WHERE id=@new_id;	

	   DELETE FROM marketcreators_datahw.h_overbidding WHERE fecha = @v_fecha;
	   DELETE FROM marketcreators_datahw.h_reportes WHERE evaluation_id=@new_id;
	   
	   INSERT INTO marketcreators_datahw.h_overbidding (fecha, año, mes, csb, penalizacion, type, promedio)
       SELECT cast(evaluation_year_month as date), 
              YEAR(evaluation_year_month) as año, 
              month(evaluation_year_month) as mes, 
              mc.csb, 
              pbv.overbidding as penalizacion,
              media."type" as type,
              media.media as promedio
         FROM marketcreators_evaluation.primary_bidding_values pbv , marketcreators_evaluation.evaluation e, marketcreators_creatordata.market_creator mc,
              (SELECT evaluation_id, b."type" , AVG(pbv.overbidding ) as media
                 FROM marketcreators_evaluation.primary_bidding_values pbv, marketcreators_evaluation.bidding b 
                WHERE b.id=pbv.bidding_id 
               GROUP BY evaluation_id,b."type") as media
        WHERE pbv.evaluation_id = media.evaluation_id 
          AND e.id = pbv.evaluation_id 
          AND pbv.market_creator_id = mc.id
          AND cast(evaluation_year_month as date)=@v_fecha;  
                  
       INSERT INTO marketcreators_datahw.aux_evaluation_data (csb, anual, month, joint_monthly, letter, bonds, bonds_subscription, bonds_quoting, bonds_trading, bonds_electronic, bonds_tier, bonds_clients, bonds_strips, bonds_qualitative, bonds_round, letter_subscription, letter_quoting, letter_trading, letter_electronic, letter_tier, letter_clients, letter_qualitative, letter_round, evaluation_id, status)
       SELECT csb,
              anual,
              [month],      
              joint_monthly,
              letter,
              bonds,
              SUM(bonds_subscription) AS bonds_subscription,
              SUM(bonds_quoting) AS bonds_quoting,
              SUM(bonds_trading) AS bonds_trading,
              SUM(bonds_electronic) AS bonds_electronic,
              SUM(bonds_tier) AS bonds_tier,
              SUM(bonds_clients) AS bonds_clients,
              SUM(bonds_strips) AS bonds_strips,
              SUM(bonds_qualitative) AS bonds_qualitative,
              SUM(bonds_second_round) AS bonds_second_round,
              SUM(letter_subscription) AS letter_subscription,
              SUM(letter_quoting) AS letter_quoting,
              SUM(letter_trading) AS letter_trading,
              SUM(letter_electronic) AS letter_electronic,
              SUM(letter_tier) AS letter_tier,
              SUM(letter_clients) AS letter_clients,
              SUM(letter_qualitative) AS letter_qualitative,
              SUM(letter_second_round) AS letter_second_round,
              evaluation_id,
              status   
         FROM (SELECT CAST(mc.csb AS NUMERIC)                                                                                   AS csb,
                      CAST(YEAR(evaluation_year_month) AS numeric)                                                              AS anual,
                      FORMAT(evaluation_year_month, 'MMMM')                                                                     AS [month],       
                      gev.total_evaluation                                                                                      AS joint_monthly,
                      gev.total_let                                                                                             AS letter,
                      gev.total_bon                                                                                             AS bonds,
                      CASE WHEN pev.product_type='BOND'   THEN pev.total_points                                      ELSE 0 END AS bonds_subscription,
                      CASE WHEN pev.product_type='BOND'   THEN seqv.total_amount_quotation                           ELSE 0 END AS bonds_quoting,
                      CASE WHEN pev.product_type='BOND'   THEN CAST(senv.total_points AS numeric)                    ELSE 0 END AS bonds_trading,
                      CASE WHEN pev.product_type='BOND'   THEN senv.senaf_mts_point+agres_senaf_mts_point+emts_point ELSE 0 END AS bonds_electronic,
                      CASE WHEN pev.product_type='BOND'   THEN other_electronic_point+non_electronic_point           ELSE 0 END AS bonds_tier,
                      CASE WHEN pev.product_type='BOND'   THEN senv.third_point                                      ELSE 0 END AS bonds_clients,
                      CASE WHEN pev.product_type='BOND'   THEN senv.strip_point                                      ELSE 0 END AS bonds_strips,
                      CASE WHEN pev.product_type='BOND'   THEN qev.total                                             ELSE 0 END AS bonds_qualitative,
                      CASE WHEN pev.product_type='BOND'   THEN reward                                                ELSE 0 END AS bonds_second_round,
                      CASE WHEN pev.product_type='LETTER' THEN pev.total_points                                      ELSE 0 END AS letter_subscription,
                      CASE WHEN pev.product_type='LETTER' THEN seqv.total_amount_quotation                           ELSE 0 END AS letter_quoting,
                      CASE WHEN pev.product_type='LETTER' THEN CAST(senv.total_points AS numeric)                    ELSE 0 END AS letter_trading,
                      CASE WHEN pev.product_type='LETTER' THEN senv.senaf_mts_point+agres_senaf_mts_point+emts_point ELSE 0 END AS letter_electronic,
                      CASE WHEN pev.product_type='LETTER' THEN other_electronic_point+non_electronic_point           ELSE 0 END AS letter_tier,
                      CASE WHEN pev.product_type='LETTER' THEN senv.third_point                                      ELSE 0 END AS letter_clients,
                      CASE WHEN pev.product_type='LETTER' THEN qev.total                                             ELSE 0 END AS letter_qualitative,
                      CASE WHEN pev.product_type='LETTER' THEN reward                                                ELSE 0 END AS letter_second_round,
                      gev.evaluation_id,
                      e.status
                 FROM marketcreators_evaluation.general_evaluation_values gev
                 INNER JOIN marketcreators_evaluation.evaluation e ON gev.evaluation_id = e.id
                 INNER JOIN marketcreators_evaluation.primary_evaluations_values pev ON pev.evaluation_id = gev.evaluation_id AND pev.market_creator_id = gev.market_creator_id
                 INNER JOIN marketcreators_evaluation.secundary_evaluations_quote_values seqv ON pev.evaluation_id = seqv.evaluation_id AND pev.market_creator_id = seqv.market_creator_id AND pev.product_type = seqv.product_type
                 INNER JOIN marketcreators_evaluation.secundary_evaluations_neg_values senv ON pev.evaluation_id = senv.evaluation_id AND pev.market_creator_id = senv.market_creator_id AND pev.product_type = senv.product_type
                 INNER JOIN marketcreators_evaluation.qualitative_evaluations_values qev ON pev.evaluation_id = qev.evaluation_id AND pev.market_creator_id = qev.market_creator_id AND pev.product_type =qev.product_type
                 INNER JOIN marketcreators_creatordata.market_creator mc ON pev.market_creator_id = mc.id
                 INNER JOIN marketcreators_creatordata.market_creator_key mck ON e.evaluation_year_month = mck.bidding_year_month AND pev.product_type = mck.product_type AND mc.csb = mck.entity_code_csb        
                WHERE e.id=@new_id
              ) AS subquery
       GROUP BY csb,
                anual,
                [month],      
                joint_monthly,
                letter,
                bonds,
                evaluation_id,
                status;
         
       INSERT INTO marketcreators_datahw.aux_evaluation_cumulate (csb, anual, month, joint_monthly, joint_cumulate)
       SELECT csb, 
              anual, 
              month,
              joint_monthly,
              joint_monthly + COALESCE(LAG(joint_monthly) OVER (PARTITION BY csb, anual ORDER BY month), 0) as joint_cumulate
         FROM marketcreators_datahw.aux_evaluation_data
       ORDER BY csb, anual, month;
        
       INSERT INTO marketcreators_datahw.h_reportes (csb,  year, month, joint_monthly, joint_cumulative, letter, bonds, bonds_subscription, bonds_quoting, bonds_trading, bonds_electronic, bonds_tier, bonds_clients, bonds_strips, bonds_qualitative, bonds_round, letter_subscription, letter_quoting, letter_trading, letter_electronic, letter_tier, letter_clients, letter_qualitative, letter_round, month_numeric, evaluation_id, ev_status)
       SELECT aed.csb,
              aed.anual,
			  CASE aed."month"
                   WHEN 'January' THEN 'Enero'
                   WHEN 'February' THEN 'Febrero'
                   WHEN 'March' THEN 'Marzo'
                   WHEN 'April' THEN 'Abril'
                   WHEN 'May' THEN 'Mayo'
                   WHEN 'June' THEN 'Junio'
                   WHEN 'July' THEN 'Julio'
                   WHEN 'August' THEN 'Agosto'
                   WHEN 'September' THEN 'Septiembre'
                   WHEN 'October' THEN 'Octubre'
                   WHEN 'November' THEN 'Noviembre'
                   WHEN 'December' THEN 'Diciembre'
                   ELSE aed."month"
              END                                                                                AS mes,                    
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY aed.joint_monthly DESC)   AS joint_monthly,
              RANK () OVER (PARTITION BY aed.anual            ORDER BY joint_cumulate DESC)      AS joint_cumulate,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter DESC)              AS letter,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds DESC)               AS bonds,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_subscription DESC)  AS bonds_subscription,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_quoting DESC)       AS bonds_quoting,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_trading DESC)       AS bonds_trading,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_electronic DESC)    AS bonds_electronic,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_tier DESC)          AS bonds_tier,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_clients DESC)       AS bonds_clients,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_strips DESC)        AS bonds_strips,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY bonds_qualitative DESC)   AS bonds_qualitative,
              bonds_second_round                                                                 AS bonds_second_round,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_subscription DESC) AS letter_subscription,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_quoting DESC)      AS letter_quoting,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_trading DESC)      AS letter_trading,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_electronic DESC)   AS letter_electronic,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_tier DESC)         AS letter_tier,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_clients DESC)      AS letter_clients,
              RANK () OVER (PARTITION BY aed.anual, aed.month ORDER BY letter_qualitative DESC)  AS letter_qualitative,
              letter_second_round                                                                AS letter_second_round,
			  CASE aed."month"
                   WHEN 'January' THEN 1
                   WHEN 'February' THEN 2
                   WHEN 'March' THEN 3
                   WHEN 'April' THEN 4
                   WHEN 'May' THEN 5
                   WHEN 'June' THEN 6
                   WHEN 'July' THEN 7
                   WHEN 'August' THEN 8
                   WHEN 'September' THEN 9
                   WHEN 'October' THEN 10
                   WHEN 'November' THEN 11
                   WHEN 'December' THEN 12
                   ELSE NULL
              END,
       	      evaluation_id,
              status
         FROM marketcreators_datahw.aux_evaluation_data aed ,
              marketcreators_datahw.aux_evaluation_cumulate aec 
        WHERE aed.csb=aec.csb 
          AND aed.anual=aec.anual 
          AND aed."month" = aec."month"
          AND aed.evaluation_id=@new_id		     
        
	END;
   END;	
END;