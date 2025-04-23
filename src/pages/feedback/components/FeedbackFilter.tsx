/* eslint-disable react-hooks/exhaustive-deps */
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import { FEEDBACK_STATUS } from '@/utils/enum/feedback/feedback-status.enum';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import queryString from 'query-string';
import { useLayoutEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

export default function FeedbackFilter() {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const navigate = useNavigate();

  useLayoutEffect(() => {
    delete params[PARAM_FIELD.PAGE];
    navigate({ search: queryString.stringify(params) });
  }, [navigate, params[PARAM_FIELD.STATUS]]);

  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSelectQueryWithLabel
          label={'Status'}
          queryKey={PARAM_FIELD.STATUS}
          options={[
            {
              label: 'Đã xử lý',
              value: FEEDBACK_STATUS.DONE,
            },
            {
              label: 'Chưa xử lý',
              value: FEEDBACK_STATUS.WAITING,
            },
          ]}
          className='w-40'
        />
      </div>
    </div>
  );
}
