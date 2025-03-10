import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-detail.reducer';

export const UserDetail = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userDetailList = useAppSelector(state => state.userDetail.entities);
  const loading = useAppSelector(state => state.userDetail.loading);

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
      <h2 id="user-detail-heading" data-cy="UserDetailHeading">
        <Translate contentKey="sTripBeApp.userDetail.home.title">User Details</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.userDetail.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-detail/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.userDetail.home.createLabel">Create new User Detail</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userDetailList && userDetailList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.userDetail.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('appUserDetail')}>
                  <Translate contentKey="sTripBeApp.userDetail.appUserDetail">App User Detail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('appUserDetail')} />
                </th>
                <th className="hand" onClick={sort('userimage')}>
                  <Translate contentKey="sTripBeApp.userDetail.userimage">Userimage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userimage')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="sTripBeApp.userDetail.phone">Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  <Translate contentKey="sTripBeApp.userDetail.gender">Gender</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('address')}>
                  <Translate contentKey="sTripBeApp.userDetail.address">Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                </th>
                <th className="hand" onClick={sort('dob')}>
                  <Translate contentKey="sTripBeApp.userDetail.dob">Dob</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('dob')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.userDetail.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userDetailList.map((userDetail, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-detail/${userDetail.id}`} color="link" size="sm">
                      {userDetail.id}
                    </Button>
                  </td>
                  <td>{userDetail.appUserDetail}</td>
                  <td>
                    {userDetail.userimage ? (
                      <div>
                        {userDetail.userimageContentType ? (
                          <a onClick={openFile(userDetail.userimageContentType, userDetail.userimage)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {userDetail.userimageContentType}, {byteSize(userDetail.userimage)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{userDetail.phone}</td>
                  <td>{userDetail.gender}</td>
                  <td>{userDetail.address}</td>
                  <td>{userDetail.dob ? <TextFormat type="date" value={userDetail.dob} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{userDetail.user ? userDetail.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-detail/${userDetail.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-detail/${userDetail.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-detail/${userDetail.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.userDetail.home.notFound">No User Details found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserDetail;
