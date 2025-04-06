import { DatePicker } from 'antd';
import dayjs, { Dayjs } from 'dayjs';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import ClockIcon from '../icons/ClockIcon';

const { RangePicker } = DatePicker;

type CustomRangePickerProps = {
  value: [number | null, number | null];
  onChange: (dates: [number | null, number | null]) => void;
  placeholder?: [string, string];
  allowClear?: boolean;
  className?: string;
};

export const CustomRangePicker = ({ value, onChange, placeholder, allowClear, className }: CustomRangePickerProps) => {
  const { t: tCommon } = useTranslation('common');

  const dayjsValue: [Dayjs | null, Dayjs | null] = [
    value[0] ? dayjs(value[0]) : null,
    value[1] ? dayjs(value[1]) : null,
  ];

  const handleChange = (dates: [Dayjs | null, Dayjs | null] | null) => {
    const formattedDates: [number | null, number | null] = [
      dates?.[0]?.valueOf() || null,
      dates?.[1]?.valueOf() || null,
    ];
    onChange(formattedDates);
  };

  return (
    <div className={twMerge('flex flex-col gap-2', className)}>
      <h2 className='text-sm font-medium'>{tCommon('label.date')}</h2>
      <RangePicker
        value={dayjsValue}
        onChange={(dates) => handleChange(dates as [Dayjs | null, Dayjs | null])}
        placeholder={placeholder}
        allowClear={allowClear}
        className='w-full p-2'
        suffixIcon={<ClockIcon className='h-4 w-4 text-gray-400' />}
      />
    </div>
  );
};
