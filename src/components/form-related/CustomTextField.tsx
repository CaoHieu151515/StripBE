import { Input } from 'antd';
import { useMemo } from 'react';
import { FieldValues, useController, UseControllerProps } from 'react-hook-form';
import { twMerge } from 'tailwind-merge';
import { ErrorMessage, ErrorMessageTranslateKey } from './ErrorMessage';

export type CustomTextFieldType<T extends FieldValues> = UseControllerProps<T> & {
  readOnly?: boolean;
  type?: FieldValues['type'];
  placeholder?: string;
  className?: string;
  hideErrorMessage?: boolean;
  suffixIcon?: React.ReactNode;
};

const CustomTextField = <T extends FieldValues>({
  name,
  control,
  type = 'text',
  disabled,
  readOnly,
  placeholder,
  className,
  suffixIcon,
  hideErrorMessage,
  ...rest
}: CustomTextFieldType<T>) => {
  const { field, fieldState } = useController({
    control,
    name,
  });

  const InputComponent = useMemo(() => {
    switch (type) {
      case 'password':
        return Input.Password;
      case 'textarea':
        return Input.TextArea;
      default:
        return Input;
    }
  }, [type]);

  return (
    <div>
      <InputComponent
        id={name}
        type={type}
        disabled={disabled}
        readOnly={readOnly}
        value={field.value}
        onChange={field.onChange}
        ref={field.ref}
        onBlur={field.onBlur}
        placeholder={placeholder}
        suffix={suffixIcon}
        className={twMerge(
          'border-1 !resize-none rounded-md border-gray-300 px-3 py-4 placeholder-gray-400 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm',
          type === 'password' && '[&>input]:placeholder-gray-400',
          className,
        )}
        {...rest}
      />
      <ErrorMessage
        translateKey={hideErrorMessage ? undefined : (fieldState.error?.message as ErrorMessageTranslateKey)}
      />
    </div>
  );
};

export default CustomTextField;
