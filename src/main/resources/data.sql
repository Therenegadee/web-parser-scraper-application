INSERT INTO storage (user_id)
VALUES
    (1);


INSERT INTO folder (name, tags, storage, parent_folder_id)
VALUES
    ('Folder 1', ARRAY['tag1', 'tag2'], 1, NULL),
    ('Subfolder 1', ARRAY['tag3'], 1, 1),
    ('Folder 2', ARRAY['tag4'], 1, NULL),
    ('Folder 3', ARRAY['tag1', 'tag2'], 1, NULL),
    ('Subfolder 4', ARRAY['tag3'], 1, 2),
    ('Subfolder 5', ARRAY['tag4'], 1, 3);

INSERT INTO user_parser_settings (name, tags, first_page_url, num_of_pages_to_parse, class_name,
                                  tag_name, css_selector_next_page, header, storage_id, parent_folder_id)
VALUES
    ('Settings 1', ARRAY['tag1', 'tag2'], 'http://example.com/page1', 5, 'class1', 'tag1', '.next-page', ARRAY['Header1', 'Header2'], 1, 1),
    ('Settings 2', ARRAY['tag1', 'tag2'], 'http://example.com/page2', 3, 'class2', 'tag2', '.next-page', ARRAY['Header3', 'Header4'], 1, 2),
    ('Settings 3', ARRAY['tag1', 'tag2'], 'http://example.com/page1', 5, 'class1', 'tag1', '.next-page', ARRAY['Header1', 'Header2'], 1, null),
    ('Settings 4', ARRAY['tag1', 'tag2'], 'http://example.com/page2', 3, 'class2', 'tag2', '.next-page', ARRAY['Header3', 'Header4'], 1, null);

INSERT INTO element_locator (name, type, path_to_locator, extra_pointer, user_parser_settings_id)
VALUES
    ('Locator 1', 'Type1', 'path/to/locator1', 'extra1', 1),
    ('Locator 2', 'Type2', 'path/to/locator2', 'extra2', 1),
    ('Locator 3', 'Type3', 'path/to/locator3', NULL, 2),
    ('Locator 1', 'Type1', 'path/to/locator1', 'extra1', 1),
    ('Locator 2', 'Type2', 'path/to/locator2', 'extra2', 1),
    ('Locator 3', 'Type3', 'path/to/locator3', NULL, 2);

INSERT INTO parser_results (date, link_to_download, output_file_type, user_parser_settings_id)
VALUES
    ('2024-01-12 12:00:00', 'http://example.com/download1', 'pdf', 1),
    ('2024-01-13 15:30:00', 'http://example.com/download2', 'csv', 2),
    ('2024-01-12 12:00:00', 'http://example.com/download1', 'pdf', 1),
    ('2024-01-13 15:30:00', 'http://example.com/download2', 'csv', 2);









