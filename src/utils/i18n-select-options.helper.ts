import { TFunction } from 'i18next';

export const translateEnumBasedOptions = (
  tCommon: TFunction<'common', undefined>,
  enumArray: string[],
  prefix: string,
) => {
  return enumArray.map((enumValue) => ({
    value: enumValue,
    label: tCommon(`${prefix}.${enumValue}` as any),
  }));
};
