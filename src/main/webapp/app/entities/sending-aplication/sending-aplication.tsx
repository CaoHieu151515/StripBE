import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './sending-aplication.reducer';

export const SendingAplication = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const sendingAplicationList = useAppSelector(state => state.sendingAplication.entities);
  const loading = useAppSelector(state => state.sendingAplication.loading);

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
      <h2 id="sending-aplication-heading" data-cy="SendingAplicationHeading">
        <Translate contentKey="sTripBeApp.sendingAplication.home.title">Sending Aplications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.sendingAplication.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/sending-aplication/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.sendingAplication.home.createLabel">Create new Sending Aplication</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sendingAplicationList && sendingAplicationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.sendingAplication.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('apliID')}>
                  <Translate contentKey="sTripBeApp.sendingAplication.apliID">Apli ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('apliID')} />
                </th>
                <th className="hand" onClick={sort('sendApplicationType')}>
                  <Translate contentKey="sTripBeApp.sendingAplication.sendApplicationType">Send Application Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sendApplicationType')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="sTripBeApp.sendingAplication.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('img')}>
                  <Translate contentKey="sTripBeApp.sendingAplication.img">Img</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('img')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.sendingAplication.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sendingAplicationList.map((sendingAplication, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sending-aplication/${sendingAplication.id}`} color="link" size="sm">
                      {sendingAplication.id}
                    </Button>
                  </td>
                  <td>{sendingAplication.apliID}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.AplicationType.${sendingAplication.sendApplicationType}`} />
                  </td>
                  <td>{sendingAplication.content}</td>
                  <td>
                    {sendingAplication.img ? (
                      <div>
                        {sendingAplication.imgContentType ? (
                          <a onClick={openFile(sendingAplication.imgContentType, sendingAplication.img)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {sendingAplication.imgContentType}, {byteSize(sendingAplication.img)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{sendingAplication.user ? sendingAplication.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/sending-aplication/${sendingAplication.id}`}
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
                        to={`/sending-aplication/${sendingAplication.id}/edit`}
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
                        onClick={() => (window.location.href = `/sending-aplication/${sendingAplication.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.sendingAplication.home.notFound">No Sending Aplications found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SendingAplication;
