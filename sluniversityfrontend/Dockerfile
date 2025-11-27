# 1. Build Stage
FROM node:22-alpine AS builder
WORKDIR /app

# Install dependencies (cache optimized)
COPY package.json package-lock.json ./
RUN npm ci

# Build the app
COPY . .
RUN npm run build

# 2. Production Stage
FROM node:22-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Don't run as root (Security Best Practice)
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

# Copy ONLY what is needed for production
COPY --from=builder /app/public ./public

# Automatically leverage output traces to reduce image size
# Copy the pre-built standalone server
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

ENV PORT=3000

# Standalone mode creates a server.js file
CMD ["node", "server.js"]