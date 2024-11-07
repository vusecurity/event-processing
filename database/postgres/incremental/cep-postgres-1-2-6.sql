/*========================================================================
Changeset   : 1.2.6
========================================================================*/

DO $$
BEGIN
    -- This check is to avoid accidentally removing the rule_update table
    -- belonging to Fraud Analysis when Fraud and CEP are sharing the same database.
    -- It ensures we only drop the table if it has the expected structure for CEP.
    IF EXISTS (
        SELECT FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'rule_update'
    ) AND (
        SELECT COUNT(*)
        FROM information_schema.columns
        WHERE table_schema = 'public'
        AND table_name = 'rule_update'
    ) = 2
    AND EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
        AND table_name = 'rule_update'
        AND column_name IN ('id', 'last_update')
        GROUP BY table_name
        HAVING COUNT(DISTINCT column_name) = 2
    ) THEN
        -- Drop the table if it exists and matches our criteria
DROP TABLE public.rule_update;
RAISE NOTICE 'Table public.rule_update has been dropped.';
ELSE
        -- Notify if the table doesn't exist or doesn't match our criteria
        RAISE NOTICE 'Table public.rule_update does not exist or does not match the expected structure.';
END IF;
END $$;