/* eslint-disable react-hooks/exhaustive-deps */
import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { TRIP_STATUS } from '@/utils/enum/trip/trip-status.enum';
import queryString from 'query-string';
import { useLayoutEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export default function TripFilter() {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const navigate = useNavigate();

  useLayoutEffect(() => {
    delete params[PARAM_FIELD.PAGE];
    navigate({ search: queryString.stringify(params) });
  }, [navigate, params[PARAM_FIELD.START_LOCATION], params[PARAM_FIELD.END_LOCATION], params[PARAM_FIELD.STATUS]]);

  return (
    <div className='flex flex-wrap items-center justify-start gap-5'>
      <CustomSearchInputQueryWithLabel
        label={'Mã chuyến đi'}
        searchParamName={PARAM_FIELD.TRIP_HANDLE_ID}
        placeholder='Nhập mã chuyến'
      />
      <CustomSearchInputQueryWithLabel
        label={'Điểm khởi hành'}
        searchParamName={PARAM_FIELD.START_LOCATION}
        placeholder='Nhập điểm khởi hành'
      />
      <CustomSearchInputQueryWithLabel
        label={'Điểm đến'}
        searchParamName={PARAM_FIELD.END_LOCATION}
        placeholder='Nhập điểm đến'
      />
      <CustomSelectQueryWithLabel
        label={'Trạng thái'}
        queryKey={PARAM_FIELD.STATUS}
        placeholder='Chọn trạng thái'
        options={[
          {
            label: 'Đã hoàn thành',
            value: TRIP_STATUS.DONE,
          },
          {
            label: 'Chuẩn bị khởi hành',
            value: TRIP_STATUS.UPCOMING,
          },
          {
            label: 'Chờ xác nhận',
            value: TRIP_STATUS.CONFIRMING,
          },
          {
            label: 'Đang đi',
            value: TRIP_STATUS.ON_GOING,
          },
          {
            label: 'Đã hủy',
            value: TRIP_STATUS.CANCEL,
          },
          {
            label: 'Đã từ chối',
            value: TRIP_STATUS.REJECTED,
          },
          {
            label: 'Đã gửi lại',
            value: TRIP_STATUS.RESEND,
          },
        ]}
        className='w-40'
      />
    </div>
  );
}
