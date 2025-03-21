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

import { getEntities } from './driver-package-subscription.reducer';

export const DriverPackageSubscription = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const driverPackageSubscriptionList = useAppSelector(state => state.driverPackageSubscription.entities);
  const loading = useAppSelector(state => state.driverPackageSubscription.loading);

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
      <h2 id="driver-package-subscription-heading" data-cy="DriverPackageSubscriptionHeading">
        <Translate contentKey="sTripBeApp.driverPackageSubscription.home.title">Driver Package Subscriptions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.driverPackageSubscription.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/driver-package-subscription/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.driverPackageSubscription.home.createLabel">Create new Driver Package Subscription</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {driverPackageSubscriptionList && driverPackageSubscriptionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.id">Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('purchaseDate')}>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.purchaseDate">Purchase Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('purchaseDate')} />
                </th>
                <th className="hand" onClick={sort('expirationDate')}>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.expirationDate">Expiration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expirationDate')} />
                </th>
                <th className="hand" onClick={sort('packagePrice')}>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.packagePrice">Package Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('packagePrice')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.driver">Driver</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.driverPackageSubscription.packageDriver">Package Driver</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {driverPackageSubscriptionList.map((driverPackageSubscription, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/driver-package-subscription/${driverPackageSubscription.id}`} color="link" size="sm">
                      {driverPackageSubscription.id}
                    </Button>
                  </td>
                  <td>
                    {driverPackageSubscription.purchaseDate ? (
                      <TextFormat type="date" value={driverPackageSubscription.purchaseDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {driverPackageSubscription.expirationDate ? (
                      <TextFormat type="date" value={driverPackageSubscription.expirationDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{driverPackageSubscription.packagePrice}</td>
                  <td>{driverPackageSubscription.active ? 'true' : 'false'}</td>
                  <td>
                    {driverPackageSubscription.driver ? (
                      <Link to={`/driver/${driverPackageSubscription.driver.id}`}>{driverPackageSubscription.driver.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {driverPackageSubscription.packageDriver ? (
                      <Link to={`/package-driver/${driverPackageSubscription.packageDriver.id}`}>
                        {driverPackageSubscription.packageDriver.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/driver-package-subscription/${driverPackageSubscription.id}`}
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
                        to={`/driver-package-subscription/${driverPackageSubscription.id}/edit`}
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
                        onClick={() => (window.location.href = `/driver-package-subscription/${driverPackageSubscription.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.driverPackageSubscription.home.notFound">No Driver Package Subscriptions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DriverPackageSubscription;
