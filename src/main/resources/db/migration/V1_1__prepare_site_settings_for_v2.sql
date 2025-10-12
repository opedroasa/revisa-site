-- Garante a linha principal (id=1) com um número provisório
INSERT INTO public.site_settings (id, whatsapp_phone)
SELECT 1, '559999999999'
WHERE NOT EXISTS (SELECT 1 FROM public.site_settings WHERE id = 1);

-- Preenche qualquer linha NULL ou vazia
UPDATE public.site_settings
SET whatsapp_phone = '559999999999'
WHERE whatsapp_phone IS NULL OR TRIM(COALESCE(whatsapp_phone, '')) = '';
