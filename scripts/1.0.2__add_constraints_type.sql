-- Version: 1.0.2
-- Descripcion: Restricciones de integridad para tabla "type" (PostgreSQL)

DO
$$
BEGIN
    IF
NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_type_category_code_by_category'
    ) THEN
ALTER TABLE "type"
    ADD CONSTRAINT uk_type_category_code_by_category
        UNIQUE (type_category_id, code);
END IF;
END $$;

DO
$$
BEGIN
    IF
NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_type_type_category'
    ) THEN
ALTER TABLE "type"
    ADD CONSTRAINT fk_type_type_category
        FOREIGN KEY (type_category_id)
            REFERENCES type_category (id);
END IF;
END $$;

