import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './passenger.reducer';

export const Passenger = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const passengerList = useAppSelector(state => state.passenger.entities);
  const loading = useAppSelector(state => state.passenger.loading);
  const totalItems = useAppSelector(state => state.passenger.totalItems);

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
      <h2 id="passenger-heading" data-cy="PassengerHeading">
        <Translate contentKey="sTripBeApp.passenger.home.title">Passengers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.passenger.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/passenger/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.passenger.home.createLabel">Create new Passenger</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {passengerList && passengerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.passenger.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('passengerID')}>
                  <Translate contentKey="sTripBeApp.passenger.passengerID">Passenger ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passengerID')} />
                </th>
                <th className="hand" onClick={sort('startLoca')}>
                  <Translate contentKey="sTripBeApp.passenger.startLoca">Start Loca</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startLoca')} />
                </th>
                <th className="hand" onClick={sort('endLoca')}>
                  <Translate contentKey="sTripBeApp.passenger.endLoca">End Loca</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endLoca')} />
                </th>
                <th className="hand" onClick={sort('amountApproveFee')}>
                  <Translate contentKey="sTripBeApp.passenger.amountApproveFee">Amount Approve Fee</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amountApproveFee')} />
                </th>
                <th className="hand" onClick={sort('luggageImg')}>
                  <Translate contentKey="sTripBeApp.passenger.luggageImg">Luggage Img</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('luggageImg')} />
                </th>
                <th className="hand" onClick={sort('luggageDescription')}>
                  <Translate contentKey="sTripBeApp.passenger.luggageDescription">Luggage Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('luggageDescription')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="sTripBeApp.passenger.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sTripBeApp.passenger.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('pickUpTime')}>
                  <Translate contentKey="sTripBeApp.passenger.pickUpTime">Pick Up Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pickUpTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="sTripBeApp.passenger.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th className="hand" onClick={sort('checkIn')}>
                  <Translate contentKey="sTripBeApp.passenger.checkIn">Check In</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkIn')} />
                </th>
                <th className="hand" onClick={sort('checkInTime')}>
                  <Translate contentKey="sTripBeApp.passenger.checkInTime">Check In Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkInTime')} />
                </th>
                <th className="hand" onClick={sort('checkOut')}>
                  <Translate contentKey="sTripBeApp.passenger.checkOut">Check Out</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkOut')} />
                </th>
                <th className="hand" onClick={sort('checkOutTIme')}>
                  <Translate contentKey="sTripBeApp.passenger.checkOutTIme">Check Out T Ime</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkOutTIme')} />
                </th>
                <th className="hand" onClick={sort('appliedAt')}>
                  <Translate contentKey="sTripBeApp.passenger.appliedAt">Applied At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('appliedAt')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.passenger.trip">Trip</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.passenger.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {passengerList.map((passenger, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/passenger/${passenger.id}`} color="link" size="sm">
                      {passenger.id}
                    </Button>
                  </td>
                  <td>{passenger.passengerID}</td>
                  <td>{passenger.startLoca}</td>
                  <td>{passenger.endLoca}</td>
                  <td>{passenger.amountApproveFee}</td>
                  <td>
                    {passenger.luggageImg ? (
                      <div>
                        {passenger.luggageImgContentType ? (
                          <a onClick={openFile(passenger.luggageImgContentType, passenger.luggageImg)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {passenger.luggageImgContentType}, {byteSize(passenger.luggageImg)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{passenger.luggageDescription}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.PassengerType.${passenger.type}`} />
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.PassengerStatus.${passenger.status}`} />
                  </td>
                  <td>{passenger.pickUpTime ? <TextFormat type="date" value={passenger.pickUpTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{passenger.endTime ? <TextFormat type="date" value={passenger.endTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{passenger.checkIn ? 'true' : 'false'}</td>
                  <td>
                    {passenger.checkInTime ? <TextFormat type="date" value={passenger.checkInTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{passenger.checkOut ? 'true' : 'false'}</td>
                  <td>
                    {passenger.checkOutTIme ? <TextFormat type="date" value={passenger.checkOutTIme} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{passenger.appliedAt ? <TextFormat type="date" value={passenger.appliedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{passenger.trip ? <Link to={`/trip/${passenger.trip.id}`}>{passenger.trip.id}</Link> : ''}</td>
                  <td>{passenger.user ? passenger.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/passenger/${passenger.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/passenger/${passenger.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/passenger/${passenger.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="sTripBeApp.passenger.home.notFound">No Passengers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={passengerList && passengerList.length > 0 ? '' : 'd-none'}>
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

export default Passenger;
