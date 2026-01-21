CREATE OR ALTER VIEW marketcreatores_web_bff.v_primary_auctions AS
SELECT ROW_NUMBER() OVER (ORDER BY pev.evaluation_id) AS id, 
       mc.csb, 
       e.evaluation_year_month, 
       pev.product_type, 
       pev.computable_sale, 
       sub.subastas AS total_sale,
       CASE WHEN pev.penalty <> 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END AS penalty
  FROM marketcreators_evaluation.primary_evaluations_values pev
INNER JOIN marketcreators_evaluation.evaluation e 
    ON e.id = pev.evaluation_id
   AND e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED')
INNER JOIN marketcreators_creatordata.market_creator mc 
    ON mc.id = pev.market_creator_id
INNER JOIN (SELECT pbv.evaluation_id, 
                   CAST(DATEADD(DAY, 1 - DAY(b.bid_date), CAST(b.bid_date AS DATE)) AS DATE) AS fecha, 
                   pbv.market_creator_id, 
                   b.type, 
                   COUNT(*) AS subastas
              FROM marketcreators_evaluation.primary_bidding_values pbv
            INNER JOIN marketcreators_evaluation.bidding b 
                ON b.id = pbv.bidding_id
             WHERE pbv.acap_creator = 0
             GROUP BY pbv.evaluation_id, 
                      CAST(DATEADD(DAY, 1 - DAY(b.bid_date), CAST(b.bid_date AS DATE)) AS DATE), 
                      pbv.market_creator_id, 
                      b.type
           ) sub 
    ON sub.evaluation_id = pev.evaluation_id
   AND sub.market_creator_id = pev.market_creator_id
   AND sub.type = pev.product_type;

CREATE OR ALTER VIEW marketcreatores_web_bff.v_secondary_auctions as
SELECT ROW_NUMBER() OVER (ORDER BY seqv.evaluation_id) AS id,
       mc.csb, 
       e.evaluation_year_month, 
       seqv.product_type, 
       seqv.basket, 
       CASE WHEN seqv.penalty_point_basket <> 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END AS penalty
  FROM marketcreators_evaluation.secundary_evaluations_quote_values seqv,
       marketcreators_evaluation.evaluation e,
       marketcreators_creatordata.market_creator mc 
 WHERE e.id = seqv.evaluation_id
   AND e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED')
   AND mc.id = seqv.market_creator_id;
	
CREATE OR ALTER VIEW marketcreatores_web_bff.v_primary_chart as
SELECT ROW_NUMBER() OVER (ORDER BY pev.evaluation_id) AS id, mc.csb, evaluation_year_month, pev.product_type, total_points,med.media
  FROM marketcreators_evaluation.primary_evaluations_values pev, 
       marketcreators_evaluation.evaluation e,
       marketcreators_creatordata.market_creator mc,
       (SELECT evaluation_id, product_type, AVG(total_points) as media
          FROM marketcreators_evaluation.primary_evaluations_values pev
        GROUP BY evaluation_id,product_type) med
 WHERE e.id = pev.evaluation_id 
   AND pev.evaluation_id = med.evaluation_id
   AND pev.product_type = med.product_type 
   AND pev.market_creator_id = mc.id
   AND e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED');

CREATE OR ALTER VIEW marketcreatores_web_bff.v_secondary_chart as
SELECT ROW_NUMBER() OVER (ORDER BY sev.evaluation_id) AS id,mc.csb, evaluation_year_month, sev.product_type, total_points,med.media
  FROM marketcreators_evaluation.secundary_evaluations_neg_values sev, 
       marketcreators_evaluation.evaluation e,
       marketcreators_creatordata.market_creator mc,
       (SELECT evaluation_id, product_type, AVG(cast(total_points as float)) as media
          FROM marketcreators_evaluation.secundary_evaluations_neg_values
        GROUP BY evaluation_id,product_type) med
 WHERE e.id = sev.evaluation_id 
   AND sev.evaluation_id = med.evaluation_id
   AND sev.product_type = med.product_type 
   AND sev.market_creator_id = mc.id
   AND e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED');

CREATE OR ALTER VIEW marketcreatores_web_bff.v_total_chart as
SELECT ROW_NUMBER() OVER (ORDER BY gev.evaluation_id) AS id, mc.csb, evaluation_year_month, total_evaluation,med.media
  FROM marketcreators_evaluation.general_evaluation_values gev, 
       marketcreators_evaluation.evaluation e,
       marketcreators_creatordata.market_creator mc,
       (SELECT evaluation_id, AVG(total_evaluation) as media
          FROM marketcreators_evaluation.general_evaluation_values
        GROUP BY evaluation_id) med
 WHERE e.id = gev.evaluation_id 
   AND gev.evaluation_id = med.evaluation_id
   AND gev.market_creator_id = mc.id
   AND e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED');
   
CREATE OR ALTER VIEW marketcreatores_web_bff.v_syndicated_issues AS
SELECT id, cast(csb as character) as csb, year, mes as month, divisa as currency, vencimiento as expiration, tamano as size, li 
  FROM marketcreators_datahw.h_tipo_divisa;

CREATE OR ALTER VIEW marketcreatores_web_bff.v_overbidding AS
SELECT id, fecha as date, año as year, mes as month, cast(csb as character) as csb, ABS(penalizacion) as penalty, type, ABS(promedio) as average
FROM marketcreators_datahw.h_overbidding;
  
CREATE OR ALTER VIEW marketcreatores_web_bff.v_evaluations AS 
SELECT h_reportes.id,
       h_reportes.evaluation_id,
       h_reportes.ev_status AS status,
       CAST(h_reportes.csb AS VARCHAR(50)) AS csb,
       h_reportes.year,
       h_reportes.month,
       h_reportes.month_numeric,
       h_reportes.joint_monthly,
       h_reportes.joint_cumulative AS cumulative,
       h_reportes.letter,
       CAST(NULL AS BIGINT) AS bonds,
       h_reportes.letter_subscription AS subscription,
       h_reportes.letter_quoting AS quoting,
       h_reportes.letter_trading AS trading,
       h_reportes.letter_electronic AS electronic,
       h_reportes.letter_tier AS tier,
       h_reportes.letter_clients AS clients,
       CAST(NULL AS BIGINT) AS strips,
       h_reportes.letter_qualitative AS qualitative,
       h_reportes.letter_round AS second_round,
       'LETTER' AS product_type
  FROM marketcreators_datahw.h_reportes
UNION ALL
SELECT h_reportes.id,
       h_reportes.evaluation_id,
       h_reportes.ev_status AS status,
       CAST(h_reportes.csb AS VARCHAR(50)) AS csb,
       h_reportes.year,
       h_reportes.month,
       h_reportes.month_numeric,
       h_reportes.joint_monthly,
       h_reportes.joint_cumulative AS cumulative,
       CAST(NULL AS BIGINT) AS letter,
       h_reportes.bonds,
       h_reportes.bonds_subscription AS subscription,
       h_reportes.bonds_quoting AS quoting,
       h_reportes.bonds_trading AS trading,
       h_reportes.bonds_electronic AS electronic,
       h_reportes.bonds_tier AS tier,
       h_reportes.bonds_clients AS clients,
       h_reportes.bonds_strips AS strips,
       h_reportes.bonds_qualitative AS qualitative,
       h_reportes.bonds_round AS second_round,
       'BOND' AS product_type
  FROM marketcreators_datahw.h_reportes;