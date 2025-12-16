-- Add description column to entry table if it doesn't exist

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'entry'
        AND column_name = 'description'
    ) THEN
        ALTER TABLE entry ADD COLUMN description TEXT;
        COMMENT ON COLUMN entry.description IS 'Optional description for the time entry';
    END IF;
END $$;
