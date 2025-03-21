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

import { getEntities } from './user-wallet.reducer';

export const UserWallet = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userWalletList = useAppSelector(state => state.userWallet.entities);
  const loading = useAppSelector(state => state.userWallet.loading);

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
      <h2 id="user-wallet-heading" data-cy="UserWalletHeading">
        <Translate contentKey="sTripBeApp.userWallet.home.title">User Wallets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.userWallet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-wallet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.userWallet.home.createLabel">Create new User Wallet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userWalletList && userWalletList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.userWallet.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userWallet')}>
                  <Translate contentKey="sTripBeApp.userWallet.userWallet">User Wallet</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userWallet')} />
                </th>
                <th className="hand" onClick={sort('before')}>
                  <Translate contentKey="sTripBeApp.userWallet.before">Before</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('before')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="sTripBeApp.userWallet.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('current')}>
                  <Translate contentKey="sTripBeApp.userWallet.current">Current</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('current')} />
                </th>
                <th className="hand" onClick={sort('mobifyDate')}>
                  <Translate contentKey="sTripBeApp.userWallet.mobifyDate">Mobify Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobifyDate')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.userWallet.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userWalletList.map((userWallet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-wallet/${userWallet.id}`} color="link" size="sm">
                      {userWallet.id}
                    </Button>
                  </td>
                  <td>{userWallet.userWallet}</td>
                  <td>{userWallet.before}</td>
                  <td>{userWallet.amount}</td>
                  <td>{userWallet.current}</td>
                  <td>
                    {userWallet.mobifyDate ? <TextFormat type="date" value={userWallet.mobifyDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userWallet.user ? userWallet.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-wallet/${userWallet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-wallet/${userWallet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-wallet/${userWallet.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.userWallet.home.notFound">No User Wallets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserWallet;
