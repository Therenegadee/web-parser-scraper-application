begin;

DROP TABLE IF EXISTS user_parser_settings CASCADE;
DROP TABLE IF EXISTS element_locator CASCADE;
DROP TABLE IF EXISTS parser_results CASCADE;
DROP TABLE IF EXISTS folder CASCADE;

CREATE TABLE IF NOT EXISTS storage (
    id          BIGSERIAL   PRIMARY KEY,
    user_id     BIGINT      NOT NULL
);

CREATE TABLE IF NOT EXISTS folder (
    id                  BIGSERIAL   PRIMARY KEY,
    name                VARCHAR     NOT NULL,
    tags                TEXT[],
    storage_id          BIGINT      NOT NULL,
    parent_folder_id    BIGINT,
    FOREIGN KEY (storage_id) REFERENCES storage(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_folder_id) REFERENCES folder(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_parser_settings (
    id                      BIGSERIAL   PRIMARY KEY,
    name                VARCHAR     NOT NULL,
    tags                TEXT[],
    first_page_url          VARCHAR     NOT NULL,
    num_of_pages_to_parse   INT         NOT NULL,
    class_name              VARCHAR     NOT NULL,
    tag_name                VARCHAR     NOT NULL,
    css_selector_next_page  VARCHAR     NOT NULL,
    header                  TEXT[]      NOT NULL,
    storage_id              BIGINT      NOT NULL,
    parent_folder_id        BIGINT,
    FOREIGN KEY (storage_id) REFERENCES storage(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_folder_id) REFERENCES folder(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS element_locator (
    id                          BIGSERIAL   PRIMARY KEY,
    name                        VARCHAR     NOT NULL,
    type                        VARCHAR     NOT NULL,
    path_to_locator             VARCHAR     NOT NULL,
    extra_pointer               VARCHAR,
    user_parser_settings_id     BIGINT,
    FOREIGN KEY (user_parser_settings_id) REFERENCES user_parser_settings (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parser_results (
    id                          BIGSERIAL   PRIMARY KEY,
    date                        TIMESTAMP,
    link_to_download            VARCHAR,
    output_file_type            VARCHAR,
    user_parser_settings_id     BIGINT,
    FOREIGN KEY (user_parser_settings_id) REFERENCES user_parser_settings (id) ON DELETE CASCADE
);

end;