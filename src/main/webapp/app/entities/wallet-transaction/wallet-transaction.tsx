import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './wallet-transaction.reducer';

export const WalletTransaction = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const walletTransactionList = useAppSelector(state => state.walletTransaction.entities);
  const loading = useAppSelector(state => state.walletTransaction.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="wallet-transaction-heading" data-cy="WalletTransactionHeading">
        <Translate contentKey="sTripBeApp.walletTransaction.home.title">Wallet Transactions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.walletTransaction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/wallet-transaction/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.walletTransaction.home.createLabel">Create new Wallet Transaction</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {walletTransactionList && walletTransactionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('transID')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.transID">Trans ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transID')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('walletType')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.walletType">Wallet Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('walletType')} />
                </th>
                <th className="hand" onClick={sort('transStatus')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.transStatus">Trans Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transStatus')} />
                </th>
                <th className="hand" onClick={sort('transactionThirdPartyID')}>
                  <Translate contentKey="sTripBeApp.walletTransaction.transactionThirdPartyID">Transaction Third Party ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transactionThirdPartyID')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.walletTransaction.systemWallet">System Wallet</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.walletTransaction.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.walletTransaction.userWallet">User Wallet</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.walletTransaction.systemTempWallet">System Temp Wallet</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {walletTransactionList.map((walletTransaction, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/wallet-transaction/${walletTransaction.id}`} color="link" size="sm">
                      {walletTransaction.id}
                    </Button>
                  </td>
                  <td>{walletTransaction.transID}</td>
                  <td>{walletTransaction.amount}</td>
                  <td>
                    {walletTransaction.date ? <TextFormat type="date" value={walletTransaction.date} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.WalletTransactionType.${walletTransaction.walletType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.TransactionStatus.${walletTransaction.transStatus}`} />
                  </td>
                  <td>{walletTransaction.transactionThirdPartyID}</td>
                  <td>
                    {walletTransaction.systemWallet ? (
                      <Link to={`/system-wallet/${walletTransaction.systemWallet.id}`}>{walletTransaction.systemWallet.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {walletTransaction.payment ? (
                      <Link to={`/payment/${walletTransaction.payment.id}`}>{walletTransaction.payment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {walletTransaction.userWallet ? (
                      <Link to={`/user-wallet/${walletTransaction.userWallet.id}`}>{walletTransaction.userWallet.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {walletTransaction.systemTempWallet ? (
                      <Link to={`/system-temp-wallet/${walletTransaction.systemTempWallet.id}`}>
                        {walletTransaction.systemTempWallet.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/wallet-transaction/${walletTransaction.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/wallet-transaction/${walletTransaction.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/wallet-transaction/${walletTransaction.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sTripBeApp.walletTransaction.home.notFound">No Wallet Transactions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WalletTransaction;
