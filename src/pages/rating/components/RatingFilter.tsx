import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { GENDER } from '@/utils/enum/common.enum';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { USER_ACCOUNT_STATUS } from '@/utils/enum/user-account/user-account.enum';

export default function RatingFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label={'Full Name'} searchParamName={PARAM_FIELD.FULL_NAME} />
        <CustomSearchInputQueryWithLabel label={'Phone'} searchParamName={PARAM_FIELD.PHONE} />
        <CustomSearchInputQueryWithLabel label={'Email'} searchParamName={PARAM_FIELD.EMAIL} />
        <CustomSelectQueryWithLabel
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
              label: 'Active',
              value: USER_ACCOUNT_STATUS.ACTIVE,
            },
            {
              label: 'Inactive',
              value: USER_ACCOUNT_STATUS.INACTIVE,
            },
          ]}
          className='w-40'
        />
      </div>
    </div>
  );
}
