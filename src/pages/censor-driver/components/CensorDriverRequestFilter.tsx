import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';

export default function CensorDriverRequestFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label={'Tên'} searchParamName={PARAM_FIELD.FIRST_NAME} />
        <CustomSearchInputQueryWithLabel label={'Họ'} searchParamName={PARAM_FIELD.LAST_NAME} />
        <CustomSearchInputQueryWithLabel label={'Email'} searchParamName={PARAM_FIELD.EMAIL} />
        <CustomSearchInputQueryWithLabel label={'Số Điện Thoại'} searchParamName={PARAM_FIELD.PHONE} />
        {/* <CustomSelectQueryWithLabel
          label={'Gender'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Male',
              value: GENDER.MALE,
            },
            {
              label: 'Female',
              value: GENDER.FEMALE,
            },
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Status'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Approved',
              value: CENSOR_DRIVER_STATUS.APPROVED,
            },
            {
              label: 'Pending',
              value: CENSOR_DRIVER_STATUS.PENDING,
            },
            {
              label: 'Declined',
              value: CENSOR_DRIVER_STATUS.DECLINED,
            },
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Time'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: '1 month',
              value: '1',
            },
            {
              label: '3 months',
              value: '3',
            },
            {
              label: '6 months',
              value: '6',
            },
            {
              label: '1 year',
              value: '12',
            },
          ]}
          className='w-40'
        /> */}
      </div>
    </div>
  );
}
