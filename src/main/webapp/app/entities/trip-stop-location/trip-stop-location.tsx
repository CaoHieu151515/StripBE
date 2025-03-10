import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './trip-stop-location.reducer';

export const TripStopLocation = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const tripStopLocationList = useAppSelector(state => state.tripStopLocation.entities);
  const loading = useAppSelector(state => state.tripStopLocation.loading);
  const totalItems = useAppSelector(state => state.tripStopLocation.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="trip-stop-location-heading" data-cy="TripStopLocationHeading">
        <Translate contentKey="sTripBeApp.tripStopLocation.home.title">Trip Stop Locations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.tripStopLocation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/trip-stop-location/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.tripStopLocation.home.createLabel">Create new Trip Stop Location</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tripStopLocationList && tripStopLocationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.tripStopLocation.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('stopLocaID')}>
                  <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaID">Stop Loca ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stopLocaID')} />
                </th>
                <th className="hand" onClick={sort('stopLoca')}>
                  <Translate contentKey="sTripBeApp.tripStopLocation.stopLoca">Stop Loca</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stopLoca')} />
                </th>
                <th className="hand" onClick={sort('stopLocaTime')}>
                  <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaTime">Stop Loca Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stopLocaTime')} />
                </th>
                <th className="hand" onClick={sort('stopLocaStatus')}>
                  <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaStatus">Stop Loca Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stopLocaStatus')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.tripStopLocation.trip">Trip</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tripStopLocationList.map((tripStopLocation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/trip-stop-location/${tripStopLocation.id}`} color="link" size="sm">
                      {tripStopLocation.id}
                    </Button>
                  </td>
                  <td>{tripStopLocation.stopLocaID}</td>
                  <td>{tripStopLocation.stopLoca}</td>
                  <td>
                    {tripStopLocation.stopLocaTime ? (
                      <TextFormat type="date" value={tripStopLocation.stopLocaTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{tripStopLocation.stopLocaStatus}</td>
                  <td>{tripStopLocation.trip ? <Link to={`/trip/${tripStopLocation.trip.id}`}>{tripStopLocation.trip.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/trip-stop-location/${tripStopLocation.id}`}
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
                        to={`/trip-stop-location/${tripStopLocation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/trip-stop-location/${tripStopLocation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="sTripBeApp.tripStopLocation.home.notFound">No Trip Stop Locations found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={tripStopLocationList && tripStopLocationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default TripStopLocation;
