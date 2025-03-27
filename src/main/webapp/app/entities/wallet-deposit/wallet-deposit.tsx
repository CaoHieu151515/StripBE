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

import { getEntities } from './wallet-deposit.reducer';

export const WalletDeposit = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const walletDepositList = useAppSelector(state => state.walletDeposit.entities);
  const loading = useAppSelector(state => state.walletDeposit.loading);

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
      <h2 id="wallet-deposit-heading" data-cy="WalletDepositHeading">
        <Translate contentKey="sTripBeApp.walletDeposit.home.title">Wallet Deposits</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.walletDeposit.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/wallet-deposit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.walletDeposit.home.createLabel">Create new Wallet Deposit</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {walletDepositList && walletDepositList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('bankNumber')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.bankNumber">Bank Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bankNumber')} />
                </th>
                <th className="hand" onClick={sort('nameOfBank')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.nameOfBank">Name Of Bank</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nameOfBank')} />
                </th>
                <th className="hand" onClick={sort('bank')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.bank">Bank</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bank')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sTripBeApp.walletDeposit.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.walletDeposit.userWallet">User Wallet</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {walletDepositList.map((walletDeposit, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/wallet-deposit/${walletDeposit.id}`} color="link" size="sm">
                      {walletDeposit.id}
                    </Button>
                  </td>
                  <td>{walletDeposit.bankNumber}</td>
                  <td>{walletDeposit.nameOfBank}</td>
                  <td>{walletDeposit.bank}</td>
                  <td>{walletDeposit.amount}</td>
                  <td>{walletDeposit.date ? <TextFormat type="date" value={walletDeposit.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.PaymentStatus.${walletDeposit.status}`} />
                  </td>
                  <td>
                    {walletDeposit.userWallet ? (
                      <Link to={`/user-wallet/${walletDeposit.userWallet.id}`}>{walletDeposit.userWallet.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/wallet-deposit/${walletDeposit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/wallet-deposit/${walletDeposit.id}/edit`}
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
                        onClick={() => (window.location.href = `/wallet-deposit/${walletDeposit.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.walletDeposit.home.notFound">No Wallet Deposits found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WalletDeposit;
