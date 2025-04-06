import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { CENSOR_DRIVER_STATUS } from '@/utils/enum/censor-driver/censor-driver.enum';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';

export default function CensorVehicleFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label={'Vehicle Name'} searchParamName={PARAM_FIELD.VEHICLE_NAME} />
        <CustomSearchInputQueryWithLabel label={'Vehicle Type'} searchParamName={PARAM_FIELD.VEHICLE_TYPE} />
        <CustomSearchInputQueryWithLabel label={'Vehicle Brand'} searchParamName={PARAM_FIELD.VEHICLE_BRAND} />
        <CustomSearchInputQueryWithLabel label={'Number Insead'} searchParamName={PARAM_FIELD.NUMBER_INSEAD} />
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
      </div>
    </div>
  );
}
