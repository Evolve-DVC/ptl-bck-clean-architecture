-- Version: 1.0.1
-- Descripcion: Creacion de tabla "type" (PostgreSQL)

CREATE SEQUENCE IF NOT EXISTS seq_type_id
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "type" (
    id BIGINT NOT NULL DEFAULT nextval('seq_type_id'),
    type_category_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(80) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN,
    create_by VARCHAR(80),
    create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(80),
    update_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_type PRIMARY KEY (id)
);

COMMENT ON TABLE "type" IS 'Tipos asociados a una categoria';
COMMENT ON COLUMN "type".id IS 'Identificador unico';
COMMENT ON COLUMN "type".type_category_id IS 'Categoria asociada';
COMMENT ON COLUMN "type".name IS 'Nombre del tipo';
COMMENT ON COLUMN "type".code IS 'Codigo del tipo';
COMMENT ON COLUMN "type".description IS 'Descripcion del tipo';
COMMENT ON COLUMN "type".active IS 'Estado activo/inactivo';
COMMENT ON COLUMN "type".create_by IS 'Usuario creador';
COMMENT ON COLUMN "type".create_date IS 'Fecha de creacion';
COMMENT ON COLUMN "type".update_by IS 'Usuario actualizador';
COMMENT ON COLUMN "type".update_date IS 'Fecha de actualizacion';

