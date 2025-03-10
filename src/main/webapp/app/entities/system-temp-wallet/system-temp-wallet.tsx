import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './system-temp-wallet.reducer';

export const SystemTempWallet = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const systemTempWalletList = useAppSelector(state => state.systemTempWallet.entities);
  const loading = useAppSelector(state => state.systemTempWallet.loading);

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
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="system-temp-wallet-heading" data-cy="SystemTempWalletHeading">
        <Translate contentKey="sTripBeApp.systemTempWallet.home.title">System Temp Wallets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.systemTempWallet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/system-temp-wallet/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.systemTempWallet.home.createLabel">Create new System Temp Wallet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {systemTempWalletList && systemTempWalletList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('systemWalletID')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.systemWalletID">System Wallet ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('systemWalletID')} />
                </th>
                <th className="hand" onClick={sort('before')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.before">Before</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('before')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('current')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.current">Current</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('current')} />
                </th>
                <th className="hand" onClick={sort('mobifyDate')}>
                  <Translate contentKey="sTripBeApp.systemTempWallet.mobifyDate">Mobify Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobifyDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {systemTempWalletList.map((systemTempWallet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/system-temp-wallet/${systemTempWallet.id}`} color="link" size="sm">
                      {systemTempWallet.id}
                    </Button>
                  </td>
                  <td>{systemTempWallet.systemWalletID}</td>
                  <td>{systemTempWallet.before}</td>
                  <td>{systemTempWallet.amount}</td>
                  <td>{systemTempWallet.current}</td>
                  <td>
                    {systemTempWallet.mobifyDate ? (
                      <TextFormat type="date" value={systemTempWallet.mobifyDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/system-temp-wallet/${systemTempWallet.id}`}
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
                        to={`/system-temp-wallet/${systemTempWallet.id}/edit`}
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
                        onClick={() => (window.location.href = `/system-temp-wallet/${systemTempWallet.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.systemTempWallet.home.notFound">No System Temp Wallets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SystemTempWallet;
