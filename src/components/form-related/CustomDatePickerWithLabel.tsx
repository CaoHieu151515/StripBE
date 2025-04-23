import { DATE_FORMAT, DATE_TIME_SHORT_24H_FORMAT, DATE_TIME_SHORT_FORMAT } from '@/utils/constants/date.constant';
import { DatePicker } from 'antd';
import { DatePickerProps } from 'antd/lib';
import dayjs from 'dayjs';
import { FieldValues, UseControllerProps, useController } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import CustomLabel from './CustomLabel';
import { ErrorMessage, ErrorMessageTranslateKey } from './ErrorMessage';

type CustomDatePickerWithLabelProps<T extends FieldValues> = UseControllerProps<T> &
  DatePickerProps & { label?: string; format?: string; use12Hours?: boolean; hideErrorMessage?: boolean };

const CustomDatePickerWithLabel = <T extends FieldValues>({
  name,
  control,
  required,
  label,
  showTime,
  hideErrorMessage,
  use12Hours,
  className,
  placeholder,
  format = DATE_FORMAT,
  ...rest
}: CustomDatePickerWithLabelProps<T>) => {
  const { t: tForm } = useTranslation('form');

  const { field, fieldState } = useController({
    control,
    name,
  });

  return (
    <div className='w-full'>
      {!!label && <CustomLabel required={required} htmlFor={name} value={label} />}
      <div className='w-full'>
        <DatePicker
          id={name}
          value={field.value ? dayjs(field.value) : dayjs()}
          placeholder={placeholder ?? tForm('placeholder.date')}
          showTime={showTime}
          format={showTime ? (use12Hours ? DATE_TIME_SHORT_FORMAT : DATE_TIME_SHORT_24H_FORMAT) : format}
          use12Hours={use12Hours}
          onChange={(date) => field.onChange(dayjs(date))}
          className={twMerge(
            'w-full !resize-none rounded-md border-2 border-gray-300 px-3 py-[15.5px] placeholder-gray-400 shadow-sm focus:border-red-500 focus:outline-none focus:ring-red-500 sm:text-sm',
            className,
          )}
          onBlur={field.onBlur}
          allowClear={false}
          {...rest}
        />
      </div>
      <ErrorMessage
        translateKey={hideErrorMessage ? undefined : (fieldState.error?.message as ErrorMessageTranslateKey)}
      />
    </div>
  );
};

export default CustomDatePickerWithLabel;
