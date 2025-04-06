import queryString from 'query-string';
import { FRESHDESK_PARAMS } from './constants/shared.constant';

export const getFreshdeskParams = () => {
  const params = queryString.parse(location.search);
  const state = params[FRESHDESK_PARAMS.state] as string;
  const nonce = params[FRESHDESK_PARAMS.nonce] as string;
  if (state && nonce) return { state, nonce };
};
