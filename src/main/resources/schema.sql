begin;

DROP TABLE IF EXISTS user_parser_settings CASCADE;
DROP TABLE IF EXISTS element_locator CASCADE;
DROP TABLE IF EXISTS parser_results CASCADE;
DROP TABLE IF EXISTS folder CASCADE;

CREATE TABLE IF NOT EXISTS user_parser_settings (
    id B                    IGSERIAL    PRIMARY KEY,
    first_page_url          VARCHAR     NOT NULL,
    num_of_pages_to_parse   INT         NOT NULL,
    class_name              VARCHAR     NOT NULL,
    tag_name                VARCHAR     NOT NULL,
    css_selector_next_page  VARCHAR     NOT NULL,
    header                  TEXT[]      NOT NULL,
    output_file_type        VARCHAR     NOT NULL
);

CREATE TABLE IF NOT EXISTS element_locator (
    id                          BIGSERIAL   PRIMARY KEY,
    user_parser_settings_id     BIGINT
    type                        VARCHAR     NOT NULL,
    path_to_locator             VARCHAR     NOT NULL,
    extra_pointer               VARCHAR,
    FOREIGN KEY (user_parser_settings_id) REFERENCES user_parser_settings (id)
);

CREATE TABLE IF NOT EXISTS parser_results (
    id                          BIGSERIAL   PRIMARY KEY,
    user_parser_settings_id     BIGINT,
    user_id                     BIGINT,
    link_to_download            VARCHAR
    date                        TIMESTAMP,
    link_to_download_results    VARCHAR,
    output_file_type            VARCHAR,
    FOREIGN KEY (user_parser_settings_id) REFERENCES user_parser_settings (id)
);

CREATE TABLE IF NOT EXISTS folder (
    id                  BIGSERIAL   PRIMARY KEY,
    user_id BIGINT      NOT NULL,
    parent_folder_id    BIGINT,
    FOREIGN KEY (parent_folder_id) REFERENCES folder(id),
);

CREATE TABLE IF NOT EXISTS folder_user_parser_settings (
    folder_id                   BIGINT,
    user_parser_settings_id     BIGINT,
    FOREIGN KEY (folder_id) REFERENCES folder(id),
    FOREIGN KEY (user_parser_settings_id) REFERENCES user_parser_settings(id)
);

end;