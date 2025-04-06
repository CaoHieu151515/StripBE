import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import EN_COMMON from './locales/en/common.json';

import EN_FORM from './locales/en/form.json';

import EN_SIGN_UP from './locales/en/sign_up.json';

import EN_LOGIN from './locales/en/login.json';

import EN_LAYOUT from './locales/en/layout.json';

import EN_DASHBOARD from './locales/en/dashboard.json';

export type I18nLanguage = keyof typeof i18nLanguages;
export const i18nLanguages = { en: 'English', kr: '한글' };
export const i18nDefaultLang: I18nLanguage = 'en';

const i18nNamespaces = ['common'] as const;
export type I18nNamespace = (typeof i18nNamespaces)[number];
export const i18nDefaultNs: I18nNamespace = 'common';

export const i18nResources = {
  en: {
    common: EN_COMMON,
    form: EN_FORM,
    sign_up: EN_SIGN_UP,
    login: EN_LOGIN,
    layout: EN_LAYOUT,
    dashboard: EN_DASHBOARD,
  },
} as const; // as const enable readonly

i18n.use(initReactI18next).init({
  resources: i18nResources,
  lng: i18nDefaultLang,
  fallbackLng: i18nDefaultLang,
  ns: i18nNamespaces,
  defaultNS: i18nDefaultNs,
  interpolation: {
    escapeValue: false, // react already safes from xss
  },
});

export default i18n;
