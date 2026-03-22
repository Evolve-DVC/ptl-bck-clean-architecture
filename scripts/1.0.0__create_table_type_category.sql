-- Version: 1.0.0
-- Descripcion: Creacion de tabla type_category (PostgreSQL)

CREATE SEQUENCE IF NOT EXISTS seq_type_category_id
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS type_category
(
    id
    BIGINT
    NOT
    NULL
    DEFAULT
    nextval
(
    'seq_type_category_id'
),
    name VARCHAR
(
    150
) NOT NULL,
    code VARCHAR
(
    80
) NOT NULL,
    description VARCHAR
(
    500
),
    active BOOLEAN,
    create_by VARCHAR
(
    80
),
    create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR
(
    80
),
    update_date TIMESTAMP
                          WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_type_category PRIMARY KEY
(
    id
),
    CONSTRAINT uk_type_category_code UNIQUE
(
    code
)
    );

COMMENT
ON TABLE type_category IS 'Categorias de tipos del sistema';
COMMENT
ON COLUMN type_category.id IS 'Identificador unico';
COMMENT
ON COLUMN type_category.name IS 'Nombre de la categoria';
COMMENT
ON COLUMN type_category.code IS 'Codigo unico de categoria';
COMMENT
ON COLUMN type_category.description IS 'Descripcion de la categoria';
COMMENT
ON COLUMN type_category.active IS 'Estado activo/inactivo';
COMMENT
ON COLUMN type_category.create_by IS 'Usuario creador';
COMMENT
ON COLUMN type_category.create_date IS 'Fecha de creacion';
COMMENT
ON COLUMN type_category.update_by IS 'Usuario actualizador';
COMMENT
ON COLUMN type_category.update_date IS 'Fecha de actualizacion';

