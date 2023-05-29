-- Criação da função para obter o ID do contato
CREATE OR REPLACE FUNCTION get_contact_id(contact_name text, contact_info text)
    RETURNS INTEGER AS $$
DECLARE
    contact_id INTEGER;
BEGIN
    INSERT INTO contact (name, contact_info)
    VALUES (contact_name, contact_info)
    RETURNING id INTO contact_id;

    RETURN contact_id;
END;
$$ LANGUAGE plpgsql;

-- Inserção dos registros na tabela professional
INSERT INTO professional (name, position, birth_date)
VALUES ('Flavio2', 'DESENVOLVEDOR', '1966-03-13');

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato Flavio', 'flavio@example.com'));

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato', 'flavio10@example.com'));

INSERT INTO professional (name, position, birth_date)
VALUES ('Flavio', 'SUPORTE', '1966-03-13');

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato Flavio', 'flavio@example.com'));

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato', 'contato@example.com'));

INSERT INTO professional (name, position, birth_date)
VALUES ('Robson', 'TESTER', '1966-02-16');

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 10', 'contato10@example.com'));

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 215', 'contato@example.com'));

INSERT INTO professional (name, position, birth_date)
VALUES ('Raissa', 'DESIGNER', '2000-02-16');

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 1', 'contato1@example.com'));

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 2', 'contato2@example.com'));

INSERT INTO professional (name, position, birth_date)
VALUES ('Raissa Comarella', 'DESENVOLVEDOR', '1996-11-06');

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 1', 'contato1@example.com'));

INSERT INTO professional_contacts (professional_id, contacts_id)
VALUES (currval('professional_id_seq'), get_contact_id('Contato 2', 'contato2@example.com'));
