/*========================================================================
Changeset   : 1.2.6
========================================================================*/

DECLARE @schema_db varchar(50) = 'cep';

DECLARE @sql NVARCHAR(MAX);

-- This check is to avoid accidentally removing the rule_update table
-- belonging to Fraud Analysis when Fraud and CEP are sharing the same database.
-- It ensures we only drop the table if it has the expected structure for CEP.
SET @sql = N'
IF OBJECT_ID(''' + QUOTENAME(@schema_db) + N'.rule_update'', ''U'') IS NOT NULL
    AND (
        SELECT COUNT(*)
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = ''' + @schema_db + N'''
          AND TABLE_NAME = ''rule_update''
    ) = 2
    AND EXISTS (
        SELECT 1
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = ''' + @schema_db + N'''
          AND TABLE_NAME = ''rule_update''
          AND COLUMN_NAME IN (''id'', ''last_update'')
        HAVING COUNT(DISTINCT COLUMN_NAME) = 2
    )
BEGIN
    DROP TABLE ' + QUOTENAME(@schema_db) + N'.rule_update;
END
';

EXEC sp_executesql @sql;