import * as yup from 'yup';

export const MAIL_REGEX = /^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/;
export const PASSWORD_REGEX = /^[a-zA-Z0-9!@#$%^&*()_+={}:;'"<>,.?/`~\-|\\]{6,20}$/;
export const PHONE_NUMBER_REGEX = /^[0-9]{10,11}$/;

export const baseSchema = {
  yupBoolean: yup.boolean().default(false),
  yupString: yup.string().default(''),
  yupRequiredString: yup.string().default('').required('required.default'),
  yupNumber: yup.number(),
};

export const allFormSchema = {
  username: yup.string().default('').required('required.username'),
  email: yup.string().default('').required('required.email'),
  password: yup.string().default('').required('required.password'),
  newPassword: yup.string().default('').required('required.new_password'),
  confirmPassword: yup
    .string()
    .default('')
    .required('required.confirm_password')
    .oneOf([yup.ref('password')], 'test.confirm_password'),
  fullname: yup.string().default('').required('required.name'),
  phoneNumber: yup.string().default('').required('required.phone_number').matches(PHONE_NUMBER_REGEX, {
    message: 'matches.phone_number',
  }),
  birthday: yup.string().default('').required('required.birthday'),
  packageName: yup.string().default('').required('required.package_name'),
  packageTime: yup
    .number()
    .default(1)
    .required('required.package_time')
    .min(1, 'required.package_time')
    .typeError('required.package_time'),
  packageBonusTime: yup.number().default(0).required('required.package_bonus_time'),
  packagePrice: yup.number().default(1000).required('required.package_price').typeError('required.package_price'),
};
