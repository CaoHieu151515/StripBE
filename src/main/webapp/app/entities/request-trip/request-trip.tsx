import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './request-trip.reducer';

export const RequestTrip = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const requestTripList = useAppSelector(state => state.requestTrip.entities);
  const loading = useAppSelector(state => state.requestTrip.loading);
  const totalItems = useAppSelector(state => state.requestTrip.totalItems);

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
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="request-trip-heading" data-cy="RequestTripHeading">
        <Translate contentKey="sTripBeApp.requestTrip.home.title">Request Trips</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.requestTrip.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/request-trip/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.requestTrip.home.createLabel">Create new Request Trip</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {requestTripList && requestTripList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.requestTrip.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('requestTripID')}>
                  <Translate contentKey="sTripBeApp.requestTrip.requestTripID">Request Trip ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('requestTripID')} />
                </th>
                <th className="hand" onClick={sort('startLoca')}>
                  <Translate contentKey="sTripBeApp.requestTrip.startLoca">Start Loca</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startLoca')} />
                </th>
                <th className="hand" onClick={sort('endLoca')}>
                  <Translate contentKey="sTripBeApp.requestTrip.endLoca">End Loca</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endLoca')} />
                </th>
                <th className="hand" onClick={sort('amountApproveFee')}>
                  <Translate contentKey="sTripBeApp.requestTrip.amountApproveFee">Amount Approve Fee</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amountApproveFee')} />
                </th>
                <th className="hand" onClick={sort('luggageImg')}>
                  <Translate contentKey="sTripBeApp.requestTrip.luggageImg">Luggage Img</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('luggageImg')} />
                </th>
                <th className="hand" onClick={sort('luggageDescription')}>
                  <Translate contentKey="sTripBeApp.requestTrip.luggageDescription">Luggage Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('luggageDescription')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="sTripBeApp.requestTrip.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sTripBeApp.requestTrip.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('pickUpTime')}>
                  <Translate contentKey="sTripBeApp.requestTrip.pickUpTime">Pick Up Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pickUpTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="sTripBeApp.requestTrip.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th className="hand" onClick={sort('checkIn')}>
                  <Translate contentKey="sTripBeApp.requestTrip.checkIn">Check In</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkIn')} />
                </th>
                <th className="hand" onClick={sort('checkInTime')}>
                  <Translate contentKey="sTripBeApp.requestTrip.checkInTime">Check In Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkInTime')} />
                </th>
                <th className="hand" onClick={sort('checkOut')}>
                  <Translate contentKey="sTripBeApp.requestTrip.checkOut">Check Out</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkOut')} />
                </th>
                <th className="hand" onClick={sort('checkOutTIme')}>
                  <Translate contentKey="sTripBeApp.requestTrip.checkOutTIme">Check Out T Ime</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkOutTIme')} />
                </th>
                <th className="hand" onClick={sort('appliedAt')}>
                  <Translate contentKey="sTripBeApp.requestTrip.appliedAt">Applied At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('appliedAt')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.requestTrip.trip">Trip</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.requestTrip.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {requestTripList.map((requestTrip, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/request-trip/${requestTrip.id}`} color="link" size="sm">
                      {requestTrip.id}
                    </Button>
                  </td>
                  <td>{requestTrip.requestTripID}</td>
                  <td>{requestTrip.startLoca}</td>
                  <td>{requestTrip.endLoca}</td>
                  <td>{requestTrip.amountApproveFee}</td>
                  <td>
                    {requestTrip.luggageImg ? (
                      <div>
                        {requestTrip.luggageImgContentType ? (
                          <a onClick={openFile(requestTrip.luggageImgContentType, requestTrip.luggageImg)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {requestTrip.luggageImgContentType}, {byteSize(requestTrip.luggageImg)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{requestTrip.luggageDescription}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.PassengerType.${requestTrip.type}`} />
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.PassengerStatus.${requestTrip.status}`} />
                  </td>
                  <td>
                    {requestTrip.pickUpTime ? <TextFormat type="date" value={requestTrip.pickUpTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{requestTrip.endTime ? <TextFormat type="date" value={requestTrip.endTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{requestTrip.checkIn ? 'true' : 'false'}</td>
                  <td>
                    {requestTrip.checkInTime ? <TextFormat type="date" value={requestTrip.checkInTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{requestTrip.checkOut ? 'true' : 'false'}</td>
                  <td>
                    {requestTrip.checkOutTIme ? <TextFormat type="date" value={requestTrip.checkOutTIme} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {requestTrip.appliedAt ? <TextFormat type="date" value={requestTrip.appliedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{requestTrip.trip ? <Link to={`/trip/${requestTrip.trip.id}`}>{requestTrip.trip.id}</Link> : ''}</td>
                  <td>{requestTrip.user ? requestTrip.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/request-trip/${requestTrip.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/request-trip/${requestTrip.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/request-trip/${requestTrip.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="sTripBeApp.requestTrip.home.notFound">No Request Trips found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={requestTripList && requestTripList.length > 0 ? '' : 'd-none'}>
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

export default RequestTrip;
