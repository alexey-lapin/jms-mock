common:
- [ ] readme
- [ ] CI

frontend:
- [ ] add frontend
- [ ] composer node - headers (templatable)
- [ ] composer node - payload (templatable)
- [ ] composer node - history

backend:
- [ ] refactor to reactive stack
- [x] move src to separate module
- [x] kotlin gradle dsl
- [ ] transform frontend links
- [x] config h2
- [x] config ping events
- [ ] config mq properties
- [ ] browse queues
- [x] enable/disable receivers
- [x] enable/disable mocks
- [x] config sse (https://serverfault.com/questions/801628/for-server-sent-events-sse-what-nginx-proxy-configuration-is-appropriate)
- [ ] expand to messaging (kafka, rabbit, ...)
- [ ] add http support
- [x] interval trigger
- [ ] inline sender
- [x] multiple mock can share same trigger
- [ ] template support
- [ ] item (mock, connector, destination) description
- [ ] item (mock, connector, destination) labels
- [ ] history support (composer, receiver, sender)