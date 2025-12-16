-- Add description column to entry table

ALTER TABLE entry ADD COLUMN description TEXT;

-- Add comment to explain the column
COMMENT ON COLUMN entry.description IS 'Optional description for the time entry';
