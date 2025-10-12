-- garante que exista a linha "principal" (id=1) com um número provisório
INSERT INTO public.site_settings (id, whatsapp_phone)
SELECT 1, '559999999999'
WHERE NOT EXISTS (SELECT 1 FROM public.site_settings WHERE id = 1);

-- preenche quaisquer linhas que estejam com NULL ou vazio
UPDATE public.site_settings s
SET whatsapp_phone = COALESCE(
  NULLIF(TRIM(s.whatsapp_phone), ''),
  (SELECT whatsapp_phone FROM public.site_settings WHERE id = 1),
  '559999999999'
)
WHERE s.whatsapp_phone IS NULL OR TRIM(COALESCE(s.whatsapp_phone, '')) = '';

-- agora pode travar como NOT NULL
ALTER TABLE public.site_settings
  ALTER COLUMN whatsapp_phone SET NOT NULL;
