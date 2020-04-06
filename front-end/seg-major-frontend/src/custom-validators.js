import { helpers } from 'vuelidate/lib/validators'

export const slug = helpers.regex('slug', /^[a-z0-9-]*$/);

export const pageSlug = (value) => {
  const reservedPageSlugs = ['new'];
  return !reservedPageSlugs.includes(value);
}

