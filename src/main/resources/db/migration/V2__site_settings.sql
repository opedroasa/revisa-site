-- Garante que remove a coluna se existir (schema public)
ALTER TABLE IF EXISTS public.site_settings
  DROP COLUMN IF EXISTS about_html;

-- Mantém convenções
ALTER TABLE public.site_settings
  ALTER COLUMN whatsapp_phone SET NOT NULL;

ALTER TABLE public.site_settings
  ALTER COLUMN updated_at SET DEFAULT now();

-- Garante o singleton id=1
INSERT INTO public.site_settings (id, whatsapp_phone)
VALUES (1, '559999999999')
ON CONFLICT (id) DO NOTHING;