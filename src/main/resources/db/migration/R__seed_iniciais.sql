-- ============================
-- data.sql  (Revisa Caminhões)
-- ============================

-- ====== MARCAS (idempotente) ======
WITH marcas(nome) AS (
  VALUES
  ('Mercedes-Benz'),
  ('Volvo'),
  ('Scania'),
  ('Volkswagen Caminhões e Ônibus'),
  ('Iveco'),
  ('DAF'),
  ('Ford'),
  ('Agrale'),
  ('International'),
  ('Hino'),
  ('Foton'),
  ('JAC Motors'),
  ('Shacman'),
  ('Mitsubishi Fuso'),
  ('MAN')
)
INSERT INTO public.marca (ativo, atualizado_em, criado_em, nome)
SELECT TRUE, NOW(), NOW(), m.nome
FROM marcas m
WHERE NOT EXISTS (
  SELECT 1 FROM public.marca x WHERE LOWER(x.nome) = LOWER(m.nome)
);

-- ====== MODELOS (idempotente) ======
WITH modelos(nome_modelo, nome_marca) AS (
  VALUES
  -- MERCEDES-BENZ
  ('Accelo 815','Mercedes-Benz'),
  ('Accelo 1016','Mercedes-Benz'),
  ('Accelo 1316','Mercedes-Benz'),
  ('Atego 1419','Mercedes-Benz'),
  ('Atego 1719','Mercedes-Benz'),
  ('Atego 2426','Mercedes-Benz'),
  ('Actros 2546','Mercedes-Benz'),
  ('Actros 2651','Mercedes-Benz'),
  ('Axor 1933','Mercedes-Benz'),
  ('Axor 2544','Mercedes-Benz'),

  -- VOLVO
  ('VM 270','Volvo'),
  ('VM 330','Volvo'),
  ('FM 370','Volvo'),
  ('FM 460','Volvo'),
  ('FMX 500','Volvo'),
  ('FH 460','Volvo'),
  ('FH 500','Volvo'),
  ('FH 540','Volvo'),

  -- SCANIA
  ('P 320','Scania'),
  ('G 360','Scania'),
  ('R 450','Scania'),
  ('R 500','Scania'),
  ('S 500','Scania'),
  ('S 540','Scania'),
  ('113H 360 (clássico)','Scania'),

  -- VOLKSWAGEN CAMINHÕES E ÔNIBUS
  ('Delivery 6.160','Volkswagen Caminhões e Ônibus'),
  ('Delivery 9.170','Volkswagen Caminhões e Ônibus'),
  ('Delivery 11.180','Volkswagen Caminhões e Ônibus'),
  ('Constellation 17.190','Volkswagen Caminhões e Ônibus'),
  ('Constellation 24.280','Volkswagen Caminhões e Ônibus'),
  ('Constellation 30.330','Volkswagen Caminhões e Ônibus'),
  ('Meteor 28.460','Volkswagen Caminhões e Ônibus'),
  ('Meteor 29.520','Volkswagen Caminhões e Ônibus'),

  -- IVECO
  ('Daily 35-150','Iveco'),
  ('Daily 55-170','Iveco'),
  ('Tector 170E28','Iveco'),
  ('Tector 240E28','Iveco'),
  ('Hi-Road 440','Iveco'),
  ('Hi-Way 490','Iveco'),
  ('Stralis 480','Iveco'),

  -- DAF
  ('CF 410','DAF'),
  ('CF 85 410','DAF'),
  ('XF 105 460','DAF'),
  ('XF 530','DAF'),

  -- FORD
  ('Cargo 1119','Ford'),
  ('Cargo 1319','Ford'),
  ('Cargo 1719','Ford'),
  ('Cargo 2429','Ford'),
  ('F-350','Ford'),
  ('F-4000','Ford'),

  -- AGRALE
  ('8700','Agrale'),
  ('10000','Agrale'),
  ('13000','Agrale'),
  ('14000','Agrale'),
  ('16000','Agrale'),

  -- INTERNATIONAL
  ('9800i','International'),
  ('9200','International'),
  ('4400 (DuraStar)','International'),
  ('7600 (WorkStar)','International'),

  -- HINO
  ('300 816','Hino'),
  ('300 1016','Hino'),
  ('500 1726','Hino'),
  ('500 2429','Hino'),

  -- FOTON
  ('Aumark 3.5T','Foton'),
  ('Aumark 6.0T','Foton'),
  ('Aumark 11.0T','Foton'),
  ('Aumark 14.0T','Foton'),

  -- JAC MOTORS
  ('V260','JAC Motors'),
  ('iEV1200T (elétrico)','JAC Motors'),

  -- SHACMAN
  ('X3000 480','Shacman'),
  ('X3000 460','Shacman'),
  ('L3000 220','Shacman'),

  -- MITSUBISHI FUSO
  ('Canter 3.5T','Mitsubishi Fuso'),
  ('Canter 6.5T','Mitsubishi Fuso'),

  -- MAN
  ('TGM 26.280','MAN'),
  ('TGX 29.440','MAN')
)
INSERT INTO public.modelo (ativo, atualizado_em, criado_em, nome, marca_id)
SELECT TRUE, NOW(), NOW(), m.nome_modelo, ma.id
FROM modelos m
JOIN public.marca ma ON LOWER(ma.nome) = LOWER(m.nome_marca)
WHERE NOT EXISTS (
  SELECT 1 FROM public.modelo mo
  WHERE LOWER(mo.nome) = LOWER(m.nome_modelo) AND mo.marca_id = ma.id
);

-- ====== USUÁRIOS INICIAIS (idempotente) ======
-- senha: hash bcrypt enviado por você
INSERT INTO public.usuario (password, role, username)
SELECT '$2a$10$S5SJgI99cSHRVqHpnkzUZOgR3o8totfO72Nu9dS7hyJ1lPuasY4hW', 'ADMIN', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM public.usuario u WHERE u.username = 'admin');

INSERT INTO public.usuario (password, role, username)
SELECT '$2a$10$S5SJgI99cSHRVqHpnkzUZOgR3o8totfO72Nu9dS7hyJ1lPuasY4hW', 'ADMIN', 'pedro'
WHERE NOT EXISTS (SELECT 1 FROM public.usuario u WHERE u.username = 'pedro');
