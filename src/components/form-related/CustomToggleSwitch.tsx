import { Switch } from 'antd';
import { FieldValues, useController, UseControllerProps } from 'react-hook-form';
import { twMerge } from 'tailwind-merge';
import CustomLabel from './CustomLabel';

type CustomToggleSwitchProps<T extends FieldValues> = UseControllerProps<T> & {
  label?: string;
  required?: boolean;
  className?: string;
  classNameLabel?: string;
};

export default function CustomToggleSwitch<T extends FieldValues>({
  name,
  control,
  label,
  required,
  className,
  classNameLabel,
  ...props
}: CustomToggleSwitchProps<T>) {
  const { field } = useController({
    control,
    name,
  });

  return (
    <div className={twMerge('flex items-center gap-2', className)}>
      {label && (
        <CustomLabel
          required={required}
          htmlFor={name}
          value={label}
          className={twMerge('text-sm font-semibold', classNameLabel)}
        />
      )}
      <Switch id='toggle-switch' checked={field.value} onChange={field.onChange} {...props} />
    </div>
  );
}
