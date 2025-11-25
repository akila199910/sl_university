# 1. Build stage
FROM node:18-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

# 2. Production stage (runs Next.js)
FROM node:18-alpine AS runner

WORKDIR /app

ENV NODE_ENV=production

# Copy only the built output
COPY --from=builder /app ./

EXPOSE 3000

CMD ["npm", "start"]
