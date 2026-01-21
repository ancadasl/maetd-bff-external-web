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
INTO marketcreatores_web_bff.aux_evaluation_data
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
       WHERE e.status IN ('FINISH_PRE_FINISHED','FINISH_DRAFT_FINISHED','FINISH_CLOSED_FINISHED','FINISH_CONSOLIDATED_FINISHED')
     ) AS subquery
GROUP BY csb,
         anual,
         [month],      
         joint_monthly,
         letter,
         bonds,
         evaluation_id,
         status;

SELECT csb, 
       anual, 
       month,
       joint_monthly,
       joint_monthly + COALESCE(LAG(joint_monthly) OVER (PARTITION BY csb, anual ORDER BY month), 0) as joint_cumulate
  INTO marketcreatores_web_bff.aux_evaluation_cumulate	   
  FROM marketcreatores_web_bff.aux_evaluation_data
ORDER BY csb, anual, month;		 