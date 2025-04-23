import useGetValuesFromParams from '@/hooks/useGetValuesFromParams';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { Button, Pagination } from 'antd';
import queryString from 'query-string';
import { useLocation, useNavigate } from 'react-router-dom';

type CustomTablePaginationProps = {
  totalItems: number;
  queryKey?: string;
  isScrollAfterPageChange?: boolean;
  className?: string;
  pageSize?: number;
};

export default function CustomTablePagination({
  totalItems,
  className,
  queryKey,
  isScrollAfterPageChange = true,
  pageSize = PAGE_SIZE,
  ...props
}: CustomTablePaginationProps) {
  const page = useGetValuesFromParams([queryKey || PARAM_FIELD.CURRENT_PAGE]);
  const location = useLocation();
  const navigate = useNavigate();
  const params = queryString.parse(location.search);

  const handleChangePage = (page: number) => {
    if (isScrollAfterPageChange) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
    params[queryKey || PARAM_FIELD.CURRENT_PAGE] = page.toString();
    navigate({ search: queryString.stringify(params) });
  };

  return (
    <div className='my-1 flex items-center justify-center gap-2'>
      <Button
        className='btn btn-primary btn-sm'
        onClick={() => handleChangePage(1)}
        disabled={+page === 1 || totalItems <= pageSize || !page[0]}>
        Đầu Trang
      </Button>
      <Pagination
        align='center'
        total={totalItems}
        current={+page || 1}
        pageSize={pageSize}
        onChange={(page: number) => handleChangePage(page)}
        className={className}
        {...props}
      />
      <Button
        className='btn btn-primary btn-sm'
        onClick={() => handleChangePage(Math.ceil(totalItems / pageSize))}
        disabled={+page === Math.ceil(totalItems / pageSize) || totalItems <= pageSize}>
        Cuối Trang
      </Button>
    </div>
  );
}
