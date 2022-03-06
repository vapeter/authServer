INSERT INTO roles (role_id, name) VALUES (0, 'ADMIN');
INSERT INTO roles (role_id, name) VALUES (1, 'ROLE_USER');

INSERT INTO employees (employee_id, name, email, mobil, password)
    VALUES (0, 'Admin', 'admin@admin.hu', '+3600123456789', '$2a$10$onOxWqaZn.951Sjq9NtY8eIq6.jx/4QERRhncmfkJCz/xavDAmxxS');

INSERT INTO employees_has_roles (employee_id, role_id) VALUES (0, 0);