/*========================================================================
Changeset   : 1.2.6
========================================================================*/

DECLARE
v_table_exists NUMBER;
    v_column_count NUMBER;
    v_correct_columns NUMBER;
BEGIN
    -- This check is to avoid accidentally removing the rule_update table
    -- belonging to Fraud Analysis when Fraud and CEP are sharing the same database.
    -- It ensures we only drop the table if it has the expected structure for CEP.

    -- Check if the table exists
SELECT COUNT(*) INTO v_table_exists
FROM USER_TABLES
WHERE TABLE_NAME = 'RULE_UPDATE';

IF v_table_exists > 0 THEN
        -- Check if the table has exactly 2 columns
SELECT COUNT(*) INTO v_column_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'RULE_UPDATE';

-- Check if the table has the correct column names
SELECT COUNT(*) INTO v_correct_columns
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'RULE_UPDATE'
  AND COLUMN_NAME IN ('ID', 'LAST_UPDATE');

IF v_column_count = 2 AND v_correct_columns = 2 THEN
    -- Drop the table if it exists and matches our criteria
    EXECUTE IMMEDIATE 'DROP TABLE RULE_UPDATE';
    DBMS_OUTPUT.PUT_LINE('Table RULE_UPDATE has been dropped.');
ELSE
    DBMS_OUTPUT.PUT_LINE('Table RULE_UPDATE exists but does not match the expected structure.');
END IF;
ELSE
    DBMS_OUTPUT.PUT_LINE('Table RULE_UPDATE does not exist.');
END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/