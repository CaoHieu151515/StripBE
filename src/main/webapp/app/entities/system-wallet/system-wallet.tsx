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

import { getEntities } from './system-wallet.reducer';

export const SystemWallet = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const systemWalletList = useAppSelector(state => state.systemWallet.entities);
  const loading = useAppSelector(state => state.systemWallet.loading);

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
      <h2 id="system-wallet-heading" data-cy="SystemWalletHeading">
        <Translate contentKey="sTripBeApp.systemWallet.home.title">System Wallets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.systemWallet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/system-wallet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.systemWallet.home.createLabel">Create new System Wallet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {systemWalletList && systemWalletList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.systemWallet.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('systemWalletID')}>
                  <Translate contentKey="sTripBeApp.systemWallet.systemWalletID">System Wallet ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('systemWalletID')} />
                </th>
                <th className="hand" onClick={sort('before')}>
                  <Translate contentKey="sTripBeApp.systemWallet.before">Before</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('before')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="sTripBeApp.systemWallet.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('current')}>
                  <Translate contentKey="sTripBeApp.systemWallet.current">Current</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('current')} />
                </th>
                <th className="hand" onClick={sort('blockAmount')}>
                  <Translate contentKey="sTripBeApp.systemWallet.blockAmount">Block Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('blockAmount')} />
                </th>
                <th className="hand" onClick={sort('mobifyDate')}>
                  <Translate contentKey="sTripBeApp.systemWallet.mobifyDate">Mobify Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobifyDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {systemWalletList.map((systemWallet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/system-wallet/${systemWallet.id}`} color="link" size="sm">
                      {systemWallet.id}
                    </Button>
                  </td>
                  <td>{systemWallet.systemWalletID}</td>
                  <td>{systemWallet.before}</td>
                  <td>{systemWallet.amount}</td>
                  <td>{systemWallet.current}</td>
                  <td>{systemWallet.blockAmount}</td>
                  <td>
                    {systemWallet.mobifyDate ? <TextFormat type="date" value={systemWallet.mobifyDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/system-wallet/${systemWallet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/system-wallet/${systemWallet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/system-wallet/${systemWallet.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.systemWallet.home.notFound">No System Wallets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SystemWallet;
