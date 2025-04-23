import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';

export default function CensorDriverRequestFilter() {
  return (
    <div className='flex flex-wrap items-center justify-start gap-5'>
      <CustomSearchInputQueryWithLabel
        label={'Tên tài xế'}
        searchParamName={PARAM_FIELD.SEARCH_KEYWORD}
        placeholder='Nhập tên tài xế hoặc biển số'
      />
    </div>
  );
}
