import { useSearchParams } from 'react-router-dom';

export default function useGetValuesFromParams(keys: string[]) {
  const [searchParams] = useSearchParams();

  const params = keys.map((key) => searchParams.get(key));

  return params;
}
