import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { TRANSACTION_STATUS } from '@/utils/enum/transaction/transaction.enum';

export default function TransactionFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label={'Full Name'} searchParamName={PARAM_FIELD.FULL_NAME} />
        <CustomSelectQueryWithLabel
          label={'Payment Method'}
          queryKey={PARAM_FIELD.PAYMENT_METHOD}
          options={[
            {
              label: 'Credit Card',
              value: 'CREDIT_CARD',
            },
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Package Time'}
          queryKey={PARAM_FIELD.PACKAGE_TIME}
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
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Status'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Success',
              value: TRANSACTION_STATUS.SUCCESS,
            },
            {
              label: 'Pending',
              value: TRANSACTION_STATUS.PENDING,
            },
            {
              label: 'Failed',
              value: TRANSACTION_STATUS.FAILED,
            },
            {
              label: 'Cancelled',
              value: TRANSACTION_STATUS.CANCELLED,
            },
          ]}
          className='w-40'
        />
      </div>
    </div>
  );
}
