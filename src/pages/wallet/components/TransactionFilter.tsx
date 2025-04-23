import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { WALLET_TYPE } from '@/utils/enum/wallet/wallet-type.enum';

const WalletTypeList = [
  {
    label: 'DEPOSIT',
    value: WALLET_TYPE.DEPOSIT,
  },
  {
    label: 'WITHDRAW',
    value: WALLET_TYPE.WITHDRAW,
  },
  {
    label: 'REFUND',
    value: WALLET_TYPE.REFUND,
  },
  {
    label: 'DRIVER_CREATE_TRIP_FEE',
    value: WALLET_TYPE.DRIVER_CREATE_TRIP_FEE,
  },
  {
    label: 'DRIVER_DONE_TRIP_REFUND',
    value: WALLET_TYPE.DRIVER_DONE_TRIP_REFUND,
  },
  {
    label: 'DRIVER_DONE_TRIP_FEE',
    value: WALLET_TYPE.DRIVER_DONE_TRIP_FEE,
  },
  {
    label: 'PASSENGER_APPROVE_FEE',
    value: WALLET_TYPE.PASSENGER_APPROVE_FEE,
  },
  {
    label: 'SYSTEM_GAIN_CREATE_TRIP_FEE',
    value: WALLET_TYPE.SYSTEM_GAIN_CREATE_TRIP_FEE,
  },
  {
    label: 'SYSTEM_GAIN_PASSENGER_APPROVE_FEE',
    value: WALLET_TYPE.SYSTEM_GAIN_PASSENGER_APPROVE_FEE,
  },
  {
    label: 'SYSTEM_GAIN_DONE_TRIP_FEE',
    value: WALLET_TYPE.SYSTEM_GAIN_DONE_TRIP_FEE,
  },
  {
    label: 'DRIVER_BUY_PACKAGE',
    value: WALLET_TYPE.DRIVER_BUY_PACKAGE,
  },
  {
    label: 'SYSTEM_GAIN_PACKAGE_FEE',
    value: WALLET_TYPE.SYSTEM_GAIN_PACKAGE_FEE,
  },
  {
    label: 'SYSTEM_REFUND_TO_DRIVER_DONE_TRIP',
    value: WALLET_TYPE.SYSTEM_REFUND_TO_DRIVER_DONE_TRIP,
  },
  {
    label: 'SYSTEM_REFUND_TO_PASSENGER',
    value: WALLET_TYPE.SYSTEM_REFUND_TO_PASSENGER,
  },
];

export default function TransactionFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSelectQueryWithLabel
          label={'Loại'}
          queryKey={PARAM_FIELD.WALLET_TYPE}
          placeholder='Chọn loại'
          options={WalletTypeList}
          className='w-100'
        />
      </div>
    </div>
  );
}
