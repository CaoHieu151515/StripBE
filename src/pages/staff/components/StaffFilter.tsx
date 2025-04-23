/* eslint-disable react-hooks/exhaustive-deps */
import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import queryString from 'query-string';
import { useLayoutEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export default function StaffFilter() {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const navigate = useNavigate();

  useLayoutEffect(() => {
    delete params[PARAM_FIELD.PAGE];
    navigate({ search: queryString.stringify(params) });
  }, [
    navigate,
    params[PARAM_FIELD.FIRST_NAME],
    params[PARAM_FIELD.LAST_NAME],
    params[PARAM_FIELD.PHONE],
    params[PARAM_FIELD.EMAIL],
  ]);

  return (
    <div className='flex flex-wrap items-center justify-start gap-5'>
      <CustomSearchInputQueryWithLabel
        label={'Họ và tên đệm'}
        searchParamName={PARAM_FIELD.FIRST_NAME}
        placeholder='Nhập họ và tên đệm'
      />
      <CustomSearchInputQueryWithLabel label={'Tên'} searchParamName={PARAM_FIELD.LAST_NAME} placeholder='Nhập tên' />
      <CustomSearchInputQueryWithLabel
        label={'Số điện thoại'}
        searchParamName={PARAM_FIELD.PHONE}
        placeholder='Nhập số điện thoại'
      />
      <CustomSearchInputQueryWithLabel label={'Email'} searchParamName={PARAM_FIELD.EMAIL} placeholder='Nhập email' />
    </div>
  );
}
