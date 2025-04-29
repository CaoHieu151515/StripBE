import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { WALLET_TYPE } from '@/utils/enum/wallet/wallet-type.enum';

export const WalletTypeList = [
  {
    label: 'Tiền gửi',
    value: WALLET_TYPE.DEPOSIT,
  },
  {
    label: 'Tiền rút',
    value: WALLET_TYPE.WITHDRAW,
  },
  {
    label: 'Tiền hoàn lại',
    value: WALLET_TYPE.REFUND,
  },
  {
    label: 'Tiền tạo chuyến đi',
    value: WALLET_TYPE.DRIVER_CREATE_TRIP_FEE,
  },
  {
    label: 'Tiền hoàn trả hoàn thành chuyến đi',
    value: WALLET_TYPE.DRIVER_DONE_TRIP_REFUND,
  },
  {
    label: 'Tiền hoàn thành chuyến đi',
    value: WALLET_TYPE.DRIVER_DONE_TRIP_FEE,
  },
  {
    label: 'Tiền duyệt hành khách',
    value: WALLET_TYPE.PASSENGER_APPROVE_FEE,
  },
  {
    label: 'Tiền hệ thống nhận tạo chuyến đi',
    value: WALLET_TYPE.SYSTEM_GAIN_CREATE_TRIP_FEE,
  },
  {
    label: 'Tiền hệ thống duyệt hành khách',
    value: WALLET_TYPE.SYSTEM_GAIN_PASSENGER_APPROVE_FEE,
  },
  {
    label: 'Tiền hệ thống nhận hoàn thành chuyến đi',
    value: WALLET_TYPE.SYSTEM_GAIN_DONE_TRIP_FEE,
  },
  {
    label: 'Tiền tài xế mua gói',
    value: WALLET_TYPE.DRIVER_BUY_PACKAGE,
  },
  {
    label: 'Tiền hệ thống nhận mua gói',
    value: WALLET_TYPE.SYSTEM_GAIN_PACKAGE_FEE,
  },
  {
    label: 'Tiền hệ thống hoàn trả tài xế khi hoàn thành chuyến đi',
    value: WALLET_TYPE.SYSTEM_REFUND_TO_DRIVER_DONE_TRIP,
  },
  {
    label: 'Tiền hệ thống hoàn trả cho hành khách',
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
          className='w-96'
        />
      </div>
    </div>
  );
}
