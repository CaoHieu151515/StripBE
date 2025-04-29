import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';

export default function CensorVehicleFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label={'Tên Tài Xế'} searchParamName={PARAM_FIELD.LAST_NAME} />
        {/* <CustomSearchInputQueryWithLabel label={'Loại phương tiện'} searchParamName={PARAM_FIELD.VEHICLE_TYPE} />
        <CustomSearchInputQueryWithLabel label={'Hãng phương tiện'} searchParamName={PARAM_FIELD.VEHICLE_BRAND} />
        <CustomSearchInputQueryWithLabel label={'Biển số xe'} searchParamName={PARAM_FIELD.NUMBER_INSEAD} />
        <CustomSelectQueryWithLabel
          label={'Trạng thái'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Đã duyệt',
              value: CENSOR_DRIVER_STATUS.APPROVED,
            },
            {
              label: 'Đang chờ',
              value: CENSOR_DRIVER_STATUS.PENDING,
            },
            {
              label: 'Đã từ chối',
              value: CENSOR_DRIVER_STATUS.DECLINED,
            },
          ]}
          className='w-40'
        /> */}
      </div>
    </div>
  );
}
