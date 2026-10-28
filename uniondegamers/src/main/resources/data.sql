
/* POBLAMIENTO DE ROLES */
INSERT INTO roles(nombre) 
    VALUES  ('ROLE_ADMINISTRADOR'), ('ROLE_MODERADOR'), ('ROLE_USUARIO');

/* POBLAMIENTO DE USUARIOS */
INSERT INTO usuarios (alias, password, nombre, appat, email, is_active) 
    VALUES("trashed_drek", "123456789.aA", "alberto", "lizana", "alb.lizana@duocuc.cl", TRUE);

INSERT INTO usuarios (alias, password, nombre, appat, email, is_active) 
    VALUES("buddzOn", "123456789.aA", "juan", "rojas", "juan@gmail.com", TRUE);


/* POBLAMIENTO DE ASIGNACIONES DE ROLES A USUARIOS */
INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
CROSS JOIN roles r
WHERE u.alias = 'trashed_drek'
  AND r.nombre IN ('ROLE_ADMINISTRADOR', 'ROLE_MODERADOR', 'ROLE_USUARIO');

INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
CROSS JOIN roles r
WHERE u.alias = 'buddzOn'
  AND r.nombre IN ('ROLE_USUARIO', 'ROLE_MODERADOR');
